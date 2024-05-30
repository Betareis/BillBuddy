package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.ui.navigation.NavigationBarScreen

@Composable
fun ProfileScreen(navController: NavController) {
    Column(modifier = Modifier.padding(3.dp)) {
        Text(text = "ProfileScreen", color= Color.Black)
    //    NavigationBarScreen()
    }
}