package com.example.myapplication.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


data class UserLoginFormData(
    val username: MutableState<String>,
    val password: MutableState<String>,
)


@Composable
fun UserLoginScreen(){
    val formData by remember {
        mutableStateOf(
            UserLoginFormData(
                mutableStateOf(""),
                mutableStateOf("")
            )
        )
    }
}