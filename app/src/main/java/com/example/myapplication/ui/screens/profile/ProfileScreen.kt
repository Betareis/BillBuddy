package com.example.myapplication.ui.screens.profile

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.data.model.Group
import com.example.myapplication.data.model.User
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.ui.navigation.AvailableScreens
import com.example.myapplication.ui.navigation.BurgerMenuDrawer
import com.example.myapplication.ui.navigation.TabView
import com.example.myapplication.ui.screens.ChangePassword
import com.example.myapplication.ui.screens.ChangePasswordSection
import com.example.myapplication.ui.screens.LogoutButton
import com.example.myapplication.ui.screens.groups.GroupsViewModel
import com.example.myapplication.ui.screens.groups.toUppercaseFirstLetter
import com.example.myapplication.ui.theme.ListElementBackgroundColor
import com.example.myapplication.ui.theme.NewWhiteFontColor
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlin.contracts.contract

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
                navController = navController
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
    navController: NavController
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
            label = { Text("PayPal Address") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                profileScreenViewModel.savePayPalAddress(paypalAddress)
                Toast.makeText(context, "PayPal address saved", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Save PayPal Address")
        }
    }
}
