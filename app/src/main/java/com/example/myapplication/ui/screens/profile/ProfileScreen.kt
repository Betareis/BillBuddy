package com.example.myapplication.ui.screens.profile

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.data.model.User
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.ui.navigation.TabView
import com.example.myapplication.ui.screens.ChangePasswordSection
import com.example.myapplication.ui.screens.LogoutButton

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navController: NavController, profileScreenViewModel: ProfileScreenViewModel = hiltViewModel()
) {
    Scaffold(
        contentColor = Color.Black,
        bottomBar = { TabView(navController) },
    ) {
        Surface(
            modifier = Modifier
                .padding(top = 60.dp)
                .fillMaxSize(),
            color = Color.White,

            ) {
            ShowData(
                loadUserData = { profileScreenViewModel.getUserProfile() },
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ChangePasswordSection(navController)
                Spacer(modifier = Modifier.height(40.dp))
                PayPalAddressSection(profileScreenViewModel)
                Spacer(modifier = Modifier.height(40.dp))
                LogoutButton(navController)
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShowData(
    loadUserData: suspend () -> DataRequestWrapper<User, String, Exception>,
) {
    val userData = produceState<DataRequestWrapper<User, String, Exception>>(
        initialValue = DataRequestWrapper(state = "loading")
    ) {
        value = loadUserData()
    }.value

    if (userData.state == "loading") {
        Text(text = "Profile screen")
        CircularProgressIndicator()
    } else if (userData.data != null) {
        Log.d("DONE", "LOADING DATA DONE")
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart
        ) {
            Text(text = userData.data!!.getDisplayName())
        }
    } else {
        Text(text = "no groups found")
    }
}

@Composable
fun PayPalAddressSection(profileScreenViewModel: ProfileScreenViewModel) {
    var paypalAddress by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = paypalAddress,
            onValueChange = { paypalAddress = it },
            label = { Text("PayPal Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                profileScreenViewModel.savePayPalAddress(paypalAddress)
                Toast.makeText(context, "PayPal username saved", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Save PayPal Username")
        }
    }
}
