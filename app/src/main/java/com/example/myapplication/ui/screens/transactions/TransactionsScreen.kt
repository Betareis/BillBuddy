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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString



@Composable
fun TransactionsScreen(
    navController: NavController,
    groupId: String,
    groupName: String,
    transactionsViewModel: TransactionsViewModel = hiltViewModel()
) {
    var selectedChoice by remember {
        mutableStateOf("Transactions")
    }
    Column(modifier = Modifier.padding(3.dp)) {
        Text(text = "TransactionsScreen", color = Color.Black)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TransactionsScreenBar(navController, groupName)
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Button(
                        onClick = { selectedChoice = "Transactions" },
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(0.dp))
                            .background(Color.Transparent).height(35.dp)
                            .then(Modifier.testTag("transactionsTab")),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent, contentColor = Color.White
                        ),
                        border = BorderStroke(0.dp, Color.Transparent),
                    ) {
                        Text(
                            text = "Transactions",
                            color = if (selectedChoice == "Transactions") Color.Black else Color.LightGray,
                            maxLines = 1
                        )
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth().height(2.dp)
                            .background(if (selectedChoice == "Transactions") Color.Black else Color.LightGray)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Button(
                        onClick = { selectedChoice = "Balances" },
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(0.dp))
                            .background(Color.Transparent).height(35.dp)
                            .then(Modifier.testTag("balancesTab")),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent, contentColor = Color.Black
                        ),
                        border = BorderStroke(0.dp, Color.Transparent),
                    ) {
                        Text(
                            text = "Balances",
                            color = if (selectedChoice == "Balances") Color.Black else Color.LightGray,
                            maxLines = 1
                        )
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth().height(1.dp)
                            .background(if (selectedChoice == "Balances") Color.Black else Color.LightGray)
                    )
                }
            }
            Box(modifier = Modifier.padding(top = 20.dp)) {
                when (selectedChoice) {
                    "Transactions" -> ShowTransactionsData(loadTransactions = {
                        transactionsViewModel.getGroupTransactionsFirestore(
                            groupId
                        )
                    }, navController = navController, groupId = groupId)

                    "Balances" -> DisplayBalancesContent(transactionsViewModel, groupId)
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 100.dp).fillMaxSize()
        ) {
            IconButton(modifier = Modifier.size(50.dp) // Adjust size as needed
                .background(MainButtonColor)// Set background color to blue
                .clip(RoundedCornerShape(80)).padding(8.dp), onClick = {
                navController.navigate("${AvailableScreens.NewEntryScreen.name}/?groupId=${groupId}")
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add a Transaction" // Provide a description for accessibility
                )
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreenBar(navController: NavController, groupName: String) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showOverlay by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(modifier = Modifier.testTag("backArrow"), onClick = {
                navController.navigate(AvailableScreens.GroupsScreen.name)
            }) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack, contentDescription = "ArrowBack"
                )
            }
        },
        title = { Text(groupName) },
        actions = {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(
                    imageVector = Icons.Outlined.Menu, contentDescription = "Menu"
                )
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        val deepLink = "myapp://transactionscreen/${groupName}"
                        shareDeepLinkOnWhatsApp(context, deepLink, groupName)
                        menuExpanded = false
                    },
                    text = { Text("Share Link on Whats App") }
                )
                DropdownMenuItem(
                    onClick = {
                        val deepLink = "myapp://transactionscreen/${groupName}"
                        clipboard.setText(AnnotatedString(deepLink))
                        Toast.makeText(context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
                        menuExpanded = false
                    },
                    text = { Text("Copy link") }
                )
            }
        }
    )
}

private fun shareDeepLinkOnWhatsApp(context: Context, deepLink: String, groupName: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Hey there! Please join my group " + groupName + " on BillBuddy: \n$deepLink")
        type = "text/plain"
        setPackage("com.whatsapp")
    }

    try {
        context.startActivity(sendIntent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}





@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShowTransactionsData(
    loadTransactions: suspend () -> DataRequestWrapper<MutableList<Transaction>, String, Exception>,
    navController: NavController,
    groupId: String
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
                                //${Uri.encode(data.id)
                                //navController.navigate(AvailableScreens.ProfileScreen.name)
                                //navController.navigate(AvailableScreens.TransactionsScreen.name)
                                navController.navigate("${AvailableScreens.TransactionInfoScreen.name}/?groupId=${groupId}&transactionId=${data.id}&transactionName=${data.name}")
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
                            }/*Row(modifier = Modifier.fillMaxWidth()) {
                                Text((data.name).toUppercaseFirstLetter(), color = NewWhiteFontColor, textAlign = TextAlign.Left)
                                //Spacer(modifier = Modifier.width(100.dp))
                                Text((data.name).toUppercaseFirstLetter(), color = NewWhiteFontColor, textAlign = TextAlign.Right)
                            }*/
                        }
                    }
                }
            }
        }

        /*Box(modifier = Modifier.padding(top = 80.dp)){
            Text(text = "Add groups icon")
        }*/

    } else {
        Text(text = "no transactions found")
    }
}

@Composable
fun DisplayBalancesContent(
    transactionsViewModel: TransactionsViewModel = hiltViewModel(),
    groupId: String
) {
    val usersOfGroup = produceState<DataRequestWrapper<MutableList<User>, String, Exception>>(
        initialValue = DataRequestWrapper(state = "loading")
    ) {
        value = transactionsViewModel.getUsersOfGroup(groupId)
    }.value

    if (usersOfGroup.state == "loading") {
        CircularProgressIndicator()
    } else if (usersOfGroup.data != null && usersOfGroup.data!!.isNotEmpty()) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(usersOfGroup.data!!) { user ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(ListElementBackgroundColor)
                        .padding(16.dp)
                ) {
                    Text(text = user.firstname + " " + user.name, color = NewWhiteFontColor)
                }
            }
        }
    } else {
        Text(text = "No users found")
    }
}
