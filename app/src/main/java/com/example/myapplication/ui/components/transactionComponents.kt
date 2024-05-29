package com.example.myapplication.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.data.model.Transaction
import com.example.myapplication.data.model.Transactions
import com.example.myapplication.ui.navigation.AvailableScreens
import com.example.myapplication.ui.screens.groups.toUppercaseFirstLetter
import com.example.myapplication.ui.theme.ListElementBackgroundColor
import com.example.myapplication.ui.theme.NewWhiteFontColor

val _transaction: MutableList<Transaction> = listOf(
    Transaction("", 200.4),
    Transaction("", .4),
    Transaction("", 200.4)
).toMutableList()

@Composable
fun TransactionsViewComponent(navController: NavController, /*transactions: Transactions*/){

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            for (data in _transaction) {
                item() {
                    FilledTonalButton(
                        onClick = {
                            navController.navigate(AvailableScreens.TransactionsScreen.name)
                        },
                        colors = ButtonColors(contentColor = NewWhiteFontColor, containerColor = ListElementBackgroundColor, disabledContentColor = Color.LightGray, disabledContainerColor = Color.LightGray),
                        modifier = Modifier
                            .offset(
                                x = 30.dp,
                            )
                            .requiredWidth(width = 400.dp)
                            .requiredHeight(height = 60.dp)
                            .testTag("groupButton${data.name}"),
                        border = BorderStroke(1.dp, Color.Black),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text((data.name).toUppercaseFirstLetter(), color = NewWhiteFontColor, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}