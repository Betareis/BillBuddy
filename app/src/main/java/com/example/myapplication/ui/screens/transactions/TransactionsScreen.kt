package com.example.myapplication.ui.screens.transactions

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.data.model.Transaction
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.ui.navigation.AvailableScreens
import com.example.myapplication.ui.screens.groups.toUppercaseFirstLetter
import com.example.myapplication.ui.theme.ListElementBackgroundColor
import com.example.myapplication.ui.theme.NewWhiteFontColor
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShowTransactionsData(
    loadTransactions: suspend () -> DataRequestWrapper<MutableList<Transaction>, String, Exception>,
    navController: NavController,
    groupId: String,
    transactionsViewModel: TransactionsViewModel
) {
    val transactionsData =
        produceState<DataRequestWrapper<MutableList<Transaction>, String, Exception>>(
            initialValue = DataRequestWrapper(state = "loading")
        ) {
            value = loadTransactions()
        }.value

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    var currentUsername by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(currentUserId) {
        currentUserId?.let {
            currentUsername = currentUserId
        }
    }

    if (transactionsData.state == "loading") {
        Text(text = "Transactions screen")
        CircularProgressIndicator()
    } else if (transactionsData.data != null && transactionsData.data!!.isNotEmpty()) {
        Log.d("DONE", "LOADING DATA DONE")
        Log.d("User", currentUsername.toString())
        val totalSpent = transactionsData.data!!.sumOf { it.amount }
        val userSpent = transactionsData.data!!.filter { it.payedBy == currentUsername }
            .sumOf { it.amount }
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    content = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "My cost: ${userSpent}€", color = Color.White)
                            Text(text = "Total expenses: ${totalSpent}€", color = Color.White)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                )
            }
        ) {Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                for (data in transactionsData.data!!) {
                    item {
                        FilledTonalButton(
                            onClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    val username =
                                        transactionsViewModel.getUsernameById(data.payedBy)

                                    if (username.data!!.isNotEmpty()) {
                                        navController.navigate("${AvailableScreens.TransactionInfoScreen.name}/?groupId=${groupId}&transactionId=${data.id}&transactionName=${data.name}&transactionAmount=${data.amount}&transactionDate=${data.date}&payedBy=${username.data}")
                                    }
                                }
                            },
                            colors = ButtonColors(
                                contentColor = NewWhiteFontColor,
                                containerColor = ListElementBackgroundColor,
                                disabledContentColor = Color.LightGray,
                                disabledContainerColor = Color.LightGray
                            ),
                            modifier = Modifier
                                .padding(start = 15.dp, end = 15.dp)
                                .fillMaxWidth()
                                .align(alignment = Alignment.Center)
                                .requiredHeight(height = 60.dp)
                                .testTag("groupButton${data.name}"),
                            border = BorderStroke(1.dp, Color.Black),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 10.dp
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    (data.name).toUppercaseFirstLetter(),
                                    modifier = Modifier.weight(0.5f),
                                    color = NewWhiteFontColor
                                )
                                Spacer(modifier = Modifier.weight(0.5f))
                                Text(data.amount.toString() + "€", color = NewWhiteFontColor)
                            }
                        }
                    }
                }
            }
        }
        }

    } else {
        Text(text = "no transactions found")
    }

}