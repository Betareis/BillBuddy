package com.example.myapplication.ui.screens.transactions

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.data.model.User
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.ui.theme.ListElementBackgroundColor
import com.example.myapplication.ui.theme.NewWhiteFontColor
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.min

@Composable
fun TransactionBalancesScreen(transactionsViewModel: TransactionsViewModel, groupId: String) {
    DisplayBalancesContent(transactionsViewModel, groupId)
}

@Composable
fun DisplayBalancesContent(
    transactionsViewModel: TransactionsViewModel = hiltViewModel(), groupId: String
) {
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    val usersOfGroup = produceState<DataRequestWrapper<Map<User, Double>, String, Exception>>(
        initialValue = DataRequestWrapper(state = "loading")
    ) {
        value = transactionsViewModel.getBalancesGroup(groupId)
    }.value

    val context = LocalContext.current

    if (usersOfGroup.state == "loading") {
        CircularProgressIndicator()
    } else if (usersOfGroup.data != null && usersOfGroup.data!!.isNotEmpty()) {
        val debts = calculateDebts(usersOfGroup.data!!)
        Log.d("Debts Calculation", "Debts: $debts") // Log the debts for debugging

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(usersOfGroup.data!!.entries.toList()) { entry ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ListElementBackgroundColor)
                            .padding(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${entry.key.getDisplayName()} ${
                                    if (entry.key.id == currentUserId) "(Me)" else ""
                                }", color = NewWhiteFontColor
                            )
                            Text(
                                textAlign = TextAlign.End,
                                text = "${entry.value}",
                                color = NewWhiteFontColor
                            )
                        }
                    }
                }

                item {
                    if (debts.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Debts Summary",
                                color = NewWhiteFontColor,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            debts.forEach { debt ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(ListElementBackgroundColor)
                                        .padding(16.dp)
                                ) {
                                    Column {
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "${debt.first.getDisplayName()} owes ${debt.second.getDisplayName()}",
                                                color = NewWhiteFontColor
                                            )
                                            Text(
                                                text = "${debt.third}",
                                                color = NewWhiteFontColor,
                                                textAlign = TextAlign.End
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        if (debt.first.id == currentUserId) {
                                            Button(
                                                onClick = {
                                                    if(debt.second.getPayPalName() != null) {
                                                        val paypalMeLink =
                                                            "https://www.paypal.me/${debt.second.getPayPalName()}/${debt.third}"
                                                        val intent = Intent(
                                                            Intent.ACTION_VIEW,
                                                            Uri.parse(paypalMeLink)
                                                        )
                                                        context.startActivity(intent)
                                                    }else{
                                                        Toast.makeText(context, "${debt.second.name} has no PayPal Username", Toast.LENGTH_SHORT).show()
                                                    }
                                                },
                                                modifier = Modifier.align(Alignment.End)
                                            ) {
                                                Text("Pay with PayPal")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "No users found")
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

fun calculateDebts(balances: Map<User, Double>): List<Triple<User, User, Double>> {
    val positiveBalances = balances.filter { it.value > 0 }.toList().toMutableList()
    val negativeBalances = balances.filter { it.value < 0 }.toList().toMutableList()

    val debts = mutableListOf<Triple<User, User, Double>>()

    while (positiveBalances.isNotEmpty() && negativeBalances.isNotEmpty()) {
        val positive = positiveBalances.removeAt(0)
        val negative = negativeBalances.removeAt(0)

        val amount = min(positive.second, -negative.second)
        debts.add(Triple(negative.first, positive.first, amount))

        if (positive.second > amount) {
            positiveBalances.add(0, Pair(positive.first, positive.second - amount))
        }
        if (-negative.second > amount) {
            negativeBalances.add(0, Pair(negative.first, negative.second + amount))
        }
    }

    return debts
}
