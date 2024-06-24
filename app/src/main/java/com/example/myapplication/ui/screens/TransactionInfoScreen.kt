package com.example.myapplication.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import com.example.myapplication.ui.navigation.AvailableScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionInfoScreen(
    navController: NavController, groupId: String, transactionId: String, transactionName: String
) {

    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(modifier = Modifier.testTag("backArrow"), onClick = {
                navController.navigateUp()
            }) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack, contentDescription = "ArrowBack"
                )
            }
        },
        title = { Text(text = transactionName) },
        actions = {
            IconButton(onClick = {
                navController.navigate("${AvailableScreens.EditTransactionScreen.name}/?groupId=${groupId}&transactionId=${transactionId}&transactionName=${transactionName}")
            }) {
                Icon(
                    imageVector = Icons.Outlined.Edit, contentDescription = "Edit"
                )
            }
        }
    )
}