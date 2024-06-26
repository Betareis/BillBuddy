package com.example.myapplication.ui.screens.profile

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.myapplication.ui.screens.groups.GroupsViewModel
import com.example.myapplication.ui.screens.groups.toUppercaseFirstLetter
import com.example.myapplication.ui.theme.ListElementBackgroundColor
import com.example.myapplication.ui.theme.MainButtonColor
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
                Spacer(modifier = Modifier.height(40.dp))
                ChangePasswordSection(navController)
                Spacer(modifier = Modifier.height(40.dp))
                PayPalUsernameSection(profileScreenViewModel)
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
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InfoBox(label = "Name", value = userData.data!!.getDisplayName())
                InfoBox(label = "PayPal Username", value = userData.data!!.retrievePayPalName())
            }
        }
    } else {
        Text(text = "no username found")
    }
}

@Composable
fun InfoBox(label: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
        }
    }
}

@Composable
fun PayPalUsernameSection(profileScreenViewModel: ProfileScreenViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black,
            disabledContentColor = Color.Gray
        ),
        modifier = Modifier.background(Color.LightGray),
        onClick = { showDialog = true },
    ) {
        Text(text = "Enter PayPal Username")
    }
    if (showDialog) {
        PayPalUsernameDialog(profileScreenViewModel) {
            showDialog = false
        }
    }
}

@Composable
fun PayPalUsernameDialog(profileScreenViewModel: ProfileScreenViewModel, onDismiss: () -> Unit) {
    var paypalAddress by remember { mutableStateOf("") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "PayPal Username") },
        text = {
            Column {
                OutlinedTextField(
                    value = paypalAddress,
                    onValueChange = { paypalAddress = it },
                    label = { Text("PayPal Username") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black,
                    disabledContentColor = MainButtonColor
                ),
                modifier = Modifier.background(MainButtonColor),
                onClick = {
                    profileScreenViewModel.savePayPalAddress(paypalAddress)
                    Toast.makeText(context, "PayPal username saved", Toast.LENGTH_SHORT).show()
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black,
                    disabledContentColor = Color.Gray
                ),
                modifier = Modifier.background(Color.LightGray),
                onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}
