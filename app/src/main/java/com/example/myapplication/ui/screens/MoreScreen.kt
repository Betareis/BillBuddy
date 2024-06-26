package com.example.myapplication.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.ui.navigation.AvailableScreens
import com.example.myapplication.ui.navigation.TabView
import com.example.myapplication.ui.theme.MainButtonColor
import com.example.myapplication.ui.theme.NewWhiteFontColor
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.myapplication.ui.navigation.BurgerMenuDrawer


@Composable
fun MoreScreen(navController: NavController) {
    Scaffold(
        contentColor = Color.Black,
        bottomBar = { TabView(navController) },
        //topBar = { BurgerMenuDrawer() },
    ) { paddingValues ->
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
                ChangePasswordSection(navController)
                Spacer(modifier = Modifier.height(40.dp))
                LogoutButton(navController)
            }

        }
    }
}

@Composable
fun ChangePasswordSection(navController: NavController) {
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
        Text(text = "Change password")
    }
    if (showDialog) {
        ChangePassword(navController) {
            showDialog = false
        }
    }
}

@Composable
fun ChangePassword(navController: NavController, onDismiss: () -> Unit) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var newPassword by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    val email = auth.currentUser?.email ?: ""

    val currentPasswordVisible = remember { mutableStateOf(false) }
    val newPasswordVisible = remember { mutableStateOf(false) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Change Password") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Current Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    visualTransformation = if (currentPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = {
                            currentPasswordVisible.value = !currentPasswordVisible.value
                        }) {
                            Icon(
                                imageVector = if (currentPasswordVisible.value) Icons.Filled.Lock else Icons.Filled.Lock,
                                contentDescription = if (currentPasswordVisible.value) "Hide password" else "Show password"
                            )
                        }
                    }
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    visualTransformation = if (newPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = {
                            newPasswordVisible.value = !newPasswordVisible.value
                        }) {
                            Icon(
                                imageVector = if (newPasswordVisible.value) Icons.Filled.Lock else Icons.Filled.Lock,
                                contentDescription = if (newPasswordVisible.value) "Hide password" else "Show password"
                            )
                        }
                    }
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
                    val user = auth.currentUser
                    if (user != null && email.isNotEmpty() && currentPassword.isNotEmpty() && newPassword.isNotEmpty()) {
                        val credential = EmailAuthProvider.getCredential(email, currentPassword)
                        user.reauthenticate(credential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    user.updatePassword(newPassword)
                                        .addOnCompleteListener { updateTask ->
                                            if (updateTask.isSuccessful) {
                                                //Log.d("PasswordChange", "Password updated")
                                                navController.navigate(AvailableScreens.LoginScreen.name) {
                                                    popUpTo(AvailableScreens.MoreScreen.name) {
                                                        inclusive = true
                                                    }
                                                    Toast.makeText(
                                                        context,
                                                        "Password successfully changed.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                                onDismiss()
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "New password must be at least 6 characters long.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Current password is wrong.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                }
            ) {
                Text(text = "Update Password", Modifier.background(Color.Transparent))
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
                onClick = { onDismiss() }
            ) {
                Text(text = "Cancel", Modifier.background(Color.Transparent))
            }
        }
    )
}

@Composable
fun LogoutButton(navController: NavController) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val context = LocalContext.current

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
                    popUpTo(AvailableScreens.MoreScreen.name) { inclusive = true }
                    Toast.makeText(context, "Successfully logged out.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                //Todo: missing
                //Log.d("Critical", "Logout failed")
            }
        }) {
        Text(text = "LogOut", Modifier.background(Color.Transparent))
    }
}
