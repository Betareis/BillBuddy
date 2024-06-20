package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.domain.userManagement.LogoutUserUseCase
import com.example.myapplication.ui.navigation.AvailableScreens
import com.example.myapplication.ui.navigation.TabView
import com.example.myapplication.ui.theme.MainButtonColor
import com.example.myapplication.ui.theme.NewWhiteFontColor
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MoreScreen(navController: NavController) {
    Scaffold(
        contentColor = Color.Black,
        bottomBar = { TabView(navController) }
    ) {paddingValues->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 60.dp)
                .fillMaxSize(),
            color = Color.White,

            ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "More Screen")
                Spacer(modifier = Modifier.height(80.dp))
                LogoutButton(navController)
            }

        }
    }
}

@Composable
fun LogoutButton(navController: NavController) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = NewWhiteFontColor,
            disabledContentColor = Color.Gray
        ),
        modifier = Modifier.background(MainButtonColor),
        onClick = {
            try {
                auth.signOut()
                navController.navigate(AvailableScreens.LoginScreen.name) {
                    popUpTo(AvailableScreens.MoreScreen.name) { inclusive =true }
                }
            } catch (e: Exception) {
                //Log.d("Critical", "Logout failed")
            }
        }) {
        Text(text = "LogOut", Modifier.background(Color.Transparent))
    }
}
