package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.data.model.Transaction
import com.example.myapplication.ui.navigation.AvailableScreens
import com.example.myapplication.ui.screens.login.UserLoginFormData
import com.example.myapplication.ui.theme.ScreenBackgroundColor

val _transaction: MutableList<Transaction> = listOf(
    Transaction("t1", 200.4),
    Transaction("t2", .4),
    Transaction("t3", 200.4)
).toMutableList()


data class NewEntryFormData(
    val username: MutableState<String>,
    val password: MutableState<String>,
)
@Composable
fun NewEntryScreen(navController: NavController, groupId: String) {
    var name by rememberSaveable { mutableStateOf("") }

    val formData by remember {
        mutableStateOf(
            NewEntryFormData(
                mutableStateOf(""),
                mutableStateOf("")
            )
        )
    }
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxSize()
            .background(
                ScreenBackgroundColor
            )
    ) {
        Text(text = groupId, color = Color.White)
        Column(
            modifier = Modifier
                .padding(3.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Date") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Payed by") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                modifier = Modifier
                    .then(Modifier.testTag("backArrow"))
                    .width(200.dp),
                    /*.border( // Add border with desired properties
                        width = 2.dp,
                        color = NewWhiteFontColor,
                        shape = RoundedCornerShape(8.dp) // Example: Rounded corners
                    ),*/
                /*colors = IconButtonColors(
                    contentColor = NewWhiteFontColor,
                    containerColor = ListElementBackgroundColor,
                    disabledContentColor = Color.LightGray,
                    disabledContainerColor = Color.LightGray
                ),*/
                onClick = {
                    navController.navigate(AvailableScreens.GroupsScreen.name)
                }) {
                Row {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "AddImage",
                        tint = Color.LightGray
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        color = Color.LightGray,
                        text = "File upload",
                        modifier = Modifier.width(400.dp),
                        fontSize = 20.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
            Text(text = "For: ", color = Color.White, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(50.dp))
            LazyColumn {
                /*
                * Todo: List of the total expense of each involved group members with quantity
                * */

            }

        }


    }
}