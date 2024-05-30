package com.example.myapplication.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.data.model.DCGroup
import com.example.myapplication.data.model.DCTransaction
import com.example.myapplication.data.model.User
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.navigation.AvailableScreens

@Composable
fun getTransactions(): List<DCTransaction> {
    val users = listOf(User("peter", 10.0), User("test", 20.5)).toMutableList()
    val icon = ImageVector.vectorResource(id = R.drawable.outline_shopping_cart_24)

    return listOf(
        DCTransaction("Transaction 1", 200.4, users, users, icon),
        DCTransaction("Transaction 2", 0.4, users, users, icon),
        DCTransaction("Transaction 3", 200.4, users, users, icon)
    )
}

@Composable
fun GroupsScreen(navController: NavController) {
    val transactions = getTransactions().toMutableList()
    var groups by remember {
        mutableStateOf(listOf(
            DCGroup("Austria", transactions),
            DCGroup("Germany", transactions),
            DCGroup("Switzerland", transactions)
        ).toMutableList())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "GroupsScreen",
            color = Color.Black,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(32.dp))

        for (group in groups) {
            FilledTonalButton(
                onClick = {
                    navController.navigate(AvailableScreens.TransactionsScreen.name)
                },
                colors = ButtonDefaults.filledTonalButtonColors(),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(0.85f)
                    .height(60.dp)
                    .testTag("groupButton${group.name}"),
                border = BorderStroke(1.dp, Color.Black),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 10.dp
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(group.name, color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                groups = groups.toMutableList().apply { add(DCGroup("Neue Gruppe", transactions)) }
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.2f)
                .height(50.dp)
        ) {
            Text("+", color = Color.White)
        }
    }
}
