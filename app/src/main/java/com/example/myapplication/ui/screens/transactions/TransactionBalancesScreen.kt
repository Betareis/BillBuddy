package com.example.myapplication.ui.screens.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.data.model.User
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.ui.theme.ListElementBackgroundColor
import com.example.myapplication.ui.theme.NewWhiteFontColor
import com.google.firebase.auth.FirebaseAuth

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

    if (usersOfGroup.state == "loading") {
        CircularProgressIndicator()
    } else if (usersOfGroup.data != null && usersOfGroup.data!!.isNotEmpty()) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            for (entry in usersOfGroup.data!!) {
                item() {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ListElementBackgroundColor)
                            .padding(16.dp)
                    ) {
                        Row {
                            Text(
                                text = "${entry.key.getDisplayName()} ${
                                    if (entry.key.id.equals(
                                            currentUserId
                                        )
                                    ) "(Me)" else ""
                                }", color = NewWhiteFontColor
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                                text = "${entry.value}",
                                color = NewWhiteFontColor
                            )
                        }
                    }
                }
            }
        }
    } else {
        Text(text = "No users found")
    }
}