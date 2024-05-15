package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController


data class UserLoginFormData(
    val username: MutableState<String>,
    val password: MutableState<String>,
)


@Composable
fun LoginScreen(navController: NavController){
    val formData by remember {
        mutableStateOf(
            UserLoginFormData(
                mutableStateOf(""),
                mutableStateOf("")
            )
        )
    }

    Column {
        Text(text = "test", color = Color.Blue)
    }
}