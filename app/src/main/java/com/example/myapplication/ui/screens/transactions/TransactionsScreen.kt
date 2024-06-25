package com.example.myapplication.ui.screens.transactions

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.*
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.data.model.Transaction
import com.example.myapplication.data.model.User
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.ui.navigation.AvailableScreens
import com.example.myapplication.ui.screens.groups.toUppercaseFirstLetter
import com.example.myapplication.ui.theme.ListElementBackgroundColor
import com.example.myapplication.ui.theme.MainButtonColor
import com.example.myapplication.ui.theme.NewWhiteFontColor
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TransactionsScreen(
    navController: NavController, groupId: String, transactionsViewModel: TransactionsViewModel
) {
    ShowTransactionsData(
        loadTransactions = {
            transactionsViewModel.getGroupTransactionsFirestore(
                groupId
            )
        },
        navController = navController,
        groupId = groupId,
        transactionsViewModel = transactionsViewModel
    )
}

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

    if (transactionsData.state == "loading") {
        Text(text = "Transactions screen")
        CircularProgressIndicator()
    } else if (transactionsData.data != null && transactionsData.data!!.isNotEmpty()) {
        Log.d("DONE", "LOADING DATA DONE")
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                for (data in transactionsData.data!!) {
                    item() {
                        FilledTonalButton(
                            onClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    val username =
                                        transactionsViewModel.getUsernameById(data.payedBy)

                                    if (username!!.data!!.isNotEmpty()) {
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
                                Text("Icon!!", color = NewWhiteFontColor)
                                Spacer(modifier = Modifier.weight(0.25f))
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


    } else {
        Text(text = "no transactions found")
    }
}


