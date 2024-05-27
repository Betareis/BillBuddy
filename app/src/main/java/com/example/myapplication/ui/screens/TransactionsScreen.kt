package com.example.myapplication.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.ui.navigation.AvailableScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(navController: NavController) {
    var selectedChoice by remember {
        mutableStateOf("Transactions")
    }
    Column(modifier = Modifier.padding(3.dp)) {
        Text(text = "TransactionsScreen", color = Color.Black)

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        modifier = Modifier
                            .then(Modifier.testTag("backArrow")),
                        onClick = {
                            navController.navigate(AvailableScreens.GroupsScreen.name)
                        }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "ArrowBack"
                        )
                    }
                },
                title = { Text("Transactions") },
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Button(
                        onClick = { selectedChoice = "Transactions" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(0.dp))
                            .background(Color.Transparent)
                            .height(35.dp)
                            .then(Modifier.testTag("transactionsTab")),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(if (selectedChoice == "Transactions") Color.Black else Color.LightGray)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Button(
                        onClick = { selectedChoice = "Balances" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(0.dp))
                            .background(Color.Transparent)
                            .height(35.dp)
                            .then(Modifier.testTag("balancesTab")),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(if (selectedChoice == "Balances") Color.Black else Color.LightGray)
                    )
                }
            }
        }
    }
}

@Composable
fun DisplayTransactionsContent(){
    
}

@Composable
fun DisplayBalancesContent(){

}