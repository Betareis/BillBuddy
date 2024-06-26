package com.example.myapplication.ui.screens.transactioninfo

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.myapplication.ui.navigation.AvailableScreens
import com.example.myapplication.ui.theme.ScreenBackgroundColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionInfoScreen(
    navController: NavController,
    groupId: String,
    transactionId: String,
    transactionName: String,
    transactionAmount: Double,
    transactionDate: String,
    payedBy: String,
    transactionInfoScreenViewModel: TransactionInfoScreenViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(modifier = Modifier.testTag("backArrow"), onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack, contentDescription = "ArrowBack"
                        )
                    }
                },
                title = { Text(text = transactionName) },
            )
        },
        content = {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .background(ScreenBackgroundColor)
                    .padding(top = 70.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InfoBox(label = "Name", value = transactionName)
                    Spacer(modifier = Modifier.height(10.dp))

                    InfoBox(label = "Amount", value = "${transactionAmount}€")
                    Spacer(modifier = Modifier.height(10.dp))

                    InfoBox(label = "Date", value = transactionDate)
                    Spacer(modifier = Modifier.height(10.dp))

                    InfoBox(label = "Paid By", value = payedBy)
                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        DeleteTransaction(
                            navController = navController,
                            groupId = groupId,
                            transactionId = transactionId,
                            transactionName = transactionName,
                            transactionInfoScreenViewModel = transactionInfoScreenViewModel
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun InfoBox(label: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}

@Composable
fun DeleteTransaction(
    navController: NavController,
    groupId: String,
    transactionId: String,
    transactionName: String,
    transactionInfoScreenViewModel: TransactionInfoScreenViewModel
) {
    Button(onClick = {
        CoroutineScope(Dispatchers.Main).launch {
            transactionInfoScreenViewModel.deleteTransaction(groupId, transactionId)
        }
        navController.popBackStack()
    },
        colors = ButtonDefaults.buttonColors(Color.Red)
    ) {
        Text(text = "Delete Transaction: $transactionName")
    }
}