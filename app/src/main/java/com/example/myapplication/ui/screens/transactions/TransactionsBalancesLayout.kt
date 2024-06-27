package com.example.myapplication.ui.screens.transactions

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.ui.navigation.AvailableScreens
import com.example.myapplication.ui.theme.MainButtonColor


@Composable
fun TransactionsBalancesLayout(
    navController: NavController,
    groupId: String,
    groupName: String,
    transactionsViewModel: TransactionsViewModel = hiltViewModel()
) {
    val selectedChoice by remember {
        mutableStateOf(mutableStateOf("Transactions"))
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
            TransactionsScreenBar(navController, groupName, groupId)
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                SelectChoiceTransactions(selectedChoice, modifier = Modifier.weight(1f))

                SelectChoiceBalances(selectedChoice, modifier = Modifier.weight(1f))


            }
            Box(modifier = Modifier.padding(top = 20.dp)) {
                when (selectedChoice.value) {
                    "Transactions" -> ShowTransactionsData(loadTransactions = {
                        transactionsViewModel.getGroupTransactionsFirestore(
                            groupId
                        )
                    }, navController = navController, groupId = groupId, transactionsViewModel)

                    "Balances" -> TransactionBalancesScreen(transactionsViewModel, groupId)
                }
            }
        }
        if (selectedChoice.value == "Transactions") {
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(bottom = 55.dp)
                    .fillMaxSize()
            ) {
                IconButton(modifier = Modifier
                    .size(50.dp)
                    .background(MainButtonColor)
                    .clip(RoundedCornerShape(80))
                    .padding(8.dp), onClick = {
                    navController.navigate("${AvailableScreens.NewEntryScreen.name}/?groupId=${groupId}")
                }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add a Transaction"
                    )
                }
            }
        }
    }
}

@Composable
fun SelectChoiceTransactions(selectedChoice: MutableState<String>, modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        Button(
            onClick = { selectedChoice.value = "Transactions" },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(0.dp))
                .background(Color.Transparent)
                .height(35.dp)
                .then(Modifier.testTag("transactionsTab")),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent, contentColor = Color.White
            ),
            border = BorderStroke(0.dp, Color.Transparent),
        ) {
            Text(
                text = "Transactions",
                color = if (selectedChoice.value == "Transactions") Color.Black else Color.LightGray,
                maxLines = 1
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(if (selectedChoice.value == "Transactions") Color.Black else Color.LightGray)
        )
    }
}

@Composable
fun SelectChoiceBalances(selectedChoice: MutableState<String>, modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        Button(
            onClick = { selectedChoice.value = "Balances" },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(0.dp))
                .background(Color.Transparent)
                .height(35.dp)
                .then(Modifier.testTag("balancesTab")),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent, contentColor = Color.Black
            ),
            border = BorderStroke(0.dp, Color.Transparent),
        ) {
            Text(
                text = "Balances",
                color = if (selectedChoice.value == "Balances") Color.Black else Color.LightGray,
                maxLines = 1
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(if (selectedChoice.value == "Balances") Color.Black else Color.LightGray)
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreenBar(navController: NavController, groupName: String, groupId: String) {
    var menuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    CenterAlignedTopAppBar(navigationIcon = {
        IconButton(modifier = Modifier.testTag("backArrow"), onClick = {
            navController.navigate(AvailableScreens.GroupsScreen.name)
        }) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack, contentDescription = "ArrowBack"
            )
        }
    }, title = { Text(groupName) }, actions = {
        IconButton(onClick = { menuExpanded = true }) {
            Icon(
                imageVector = Icons.Outlined.Menu, contentDescription = "Menu"
            )
        }
        DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
            DropdownMenuItem(onClick = {
                val deepLink = "https://www.billbuddy.com/joingroup/?groupId=${groupId}"
                shareDeepLinkOnWhatsApp(context, deepLink, groupName)
                menuExpanded = false
            }, text = { Text("Share Link on Whats App") })
            DropdownMenuItem(onClick = {
                val deepLink = "https://www.billbuddy.com/joingroup/?groupId=${groupId}"
                shareDeepLinkOnDiscord(context, deepLink, groupName)
                menuExpanded = false
            }, text = { Text("Share Link on Discord") })
            DropdownMenuItem(onClick = {
                val deepLink = "https://www.billbuddy.com/joingroup/?groupId=${groupId}"
                clipboard.setText(AnnotatedString(deepLink))
                Toast.makeText(context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
                menuExpanded = false
            }, text = { Text("Copy link") })
        }
    })
}

private fun shareDeepLinkOnWhatsApp(context: Context, deepLink: String, groupName: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "Hey there! Please join my group " + groupName + " on BillBuddy: \n$deepLink"
        )
        type = "text/plain"
        setPackage("com.whatsapp")
    }

    try {
        context.startActivity(sendIntent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun shareDeepLinkOnDiscord(context: Context, deepLink: String, groupName: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "Hey there! Please join my group " + groupName + " on BillBuddy: \n$deepLink"
        )
        type = "text/plain"
        setPackage("com.discord")
    }

    try {
        context.startActivity(sendIntent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}