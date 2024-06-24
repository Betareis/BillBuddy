package com.example.myapplication.ui.screens.edittransaction

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.ui.navigation.AvailableScreens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    navController: NavController,
    groupId: String,
    transactionId: String,
    transactionName: String,
    editTransactionScreenViewModel: EditTransactionScreenViewModel = hiltViewModel()
) {

    Scaffold(contentColor = Color.Black, topBar = {
        NavigationBarEditTransactionScreen(
            navController, transactionName, groupId, transactionId
        )
    }) {
        Surface(
            modifier = Modifier
                .padding(top = 60.dp)
                .fillMaxSize(),
            color = Color.White,

            ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TextField(
                    value = "Transaction: $transactionName", onValueChange = {}, enabled = false
                )
                Column() {
                    Text(text = "EditTransactionScreen $transactionId", color = Color.Black)
                }
                TextField(
                    placeholder = { Text(text = "Amount") },
                    value = "",
                    onValueChange = {},
                )

                //!Todo: Date from firestore
                TextField(
                    value = "Date: 05.09.2024", onValueChange = {}, enabled = false
                )

                TextField(
                    value = "XXX", onValueChange = {}, enabled = false
                )
                DeleteTransaction(
                    navController = navController,
                    groupId = groupId,
                    transactionId = transactionId,
                    transactionName = transactionName,
                    editTransactionScreenViewModel = editTransactionScreenViewModel

                )
            }
        }
    }
}

@Composable
fun DeleteTransaction(
    navController: NavController,
    groupId: String,
    transactionId: String,
    transactionName: String,
    editTransactionScreenViewModel: EditTransactionScreenViewModel
) {
    Button(onClick = {
        //!Todo: Not checking if a transaction was actually deleted
        CoroutineScope(Dispatchers.Main).launch {
            editTransactionScreenViewModel.deleteTransaction(groupId, transactionId)
        }
        navController.popBackStack() // Pop once
        navController.popBackStack() // Pop again for two levels back
    }) {
        Text(text = "Delete Transaction $transactionName")
    }
}

/*********Navigation Bar************/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBarEditTransactionScreen(
    navController: NavController, transactionName: String, groupId: String, transactionId: String
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(modifier = Modifier.then(Modifier.testTag("backArrow")), onClick = {
                navController.navigate("${AvailableScreens.TransactionInfoScreen.name}/?groupId=${groupId}&transactionId=${transactionId}&transactionName=${transactionName}")
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "ArrowBack"
                )
            }
        },
        title = { Text(transactionName) },
    )
}


