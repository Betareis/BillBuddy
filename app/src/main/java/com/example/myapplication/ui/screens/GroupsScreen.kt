package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.data.model.DCGroup
import com.example.myapplication.data.model.DCTransaction
import com.example.myapplication.data.model.User;
import androidx.compose.ui.unit.sp

val _users = listOf<User>(User("peter", 10.0), User("test", 20.5)).toMutableList<User>()

val _transaction: MutableList<DCTransaction> = listOf(
    DCTransaction("", 200.4, _users, _users),
    DCTransaction("", .4, _users, _users),
    DCTransaction("", 200.4, _users, _users)
).toMutableList()

val _groups: MutableList<DCGroup> = listOf(
    DCGroup("Austria", _transaction),
    DCGroup("Austria", _transaction),
    DCGroup("Austria", _transaction),
).toMutableList()

@Composable
fun GroupsScreen(navController: NavController) {

    Column(modifier = Modifier.padding(3.dp)) {
        Text(text = "GroupsScreen", color = Color.Black)
        for (group in _groups) {
            Spacer(modifier = Modifier.height(20.dp))
            Row() {
                Text(text = group.name, color = Color.Black, fontSize = 30.sp)
            }
        }
    }
}