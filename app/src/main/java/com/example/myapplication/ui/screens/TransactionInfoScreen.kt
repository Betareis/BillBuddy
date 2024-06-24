package com.example.myapplication.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.ui.navigation.AvailableScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionInfoScreen(
    navController: NavController, groupId: String, transactionId: String, transactionName: String
) {

    Scaffold(contentColor = Color.Black,
        topBar = { NavigationBarTransactionInfoScreen(navController, transactionName) }) {
        Surface(
            modifier = Modifier
                .padding(top = 60.dp)
                .fillMaxSize(),
            color = Color.White,

            ) {
            Box() {
                /*Column(modifier = Modifier.padding(3.dp)) {
                    Text(text = "TransactionInfoScreen: $transactionId", color = Color.Black)
                }
                Column {
                    Text(text = transactionName, color = Color.Black)
                }*/
                NavigateEditTransactionScreenButton(
                    navController, groupId, transactionId, transactionName
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBarTransactionInfoScreen(navController: NavController, transactionName: String) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(modifier = Modifier.then(Modifier.testTag("backArrow")), onClick = {
                //navController.navigate(AvailableScreens.TransactionsScreen.name)
                navController.navigateUp()
                //navController.popBackStack()
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

@Composable
fun NavigateEditTransactionScreenButton(
    navController: NavController, groupId: String, transactionId: String, transactionName: String
) {
    Button(onClick = { navController.navigate("${AvailableScreens.EditTransactionScreen.name}/?groupId=${groupId}&transactionId=${transactionId}&transactionName=${transactionName}") }) {
        Text(text = "Edit Transaction XXX")
    }
}