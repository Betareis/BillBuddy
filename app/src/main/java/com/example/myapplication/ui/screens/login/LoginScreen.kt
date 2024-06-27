package com.example.myapplication.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.ui.components.EmailInputField
import com.example.myapplication.ui.components.PassInputField
import com.example.myapplication.ui.navigation.AvailableScreens

import com.example.myapplication.ui.theme.MainButtonColor
import com.example.myapplication.ui.theme.NewWhiteFontColor
import com.example.myapplication.ui.theme.ScreenBackgroundColor

data class UserLoginFormData(
    val username: MutableState<String>,
    val password: MutableState<String>,
)

@Composable
fun LoginScreen(
    navController: NavController, loginScreenViewModel: LoginScreenViewModel = viewModel()
) {
    val isPasswordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val formData by remember {
        mutableStateOf(
            UserLoginFormData(
                mutableStateOf(""),
                mutableStateOf("")
            )
        )
    }
    Column(
        modifier = Modifier
            .padding(3.dp)
            .background(ScreenBackgroundColor)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            fontSize = 40.sp,
            color = NewWhiteFontColor,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
        Spacer(modifier = Modifier.height(80.dp))
        EmailInputField(Modifier, formData.username)
        Spacer(modifier = Modifier.height(40.dp))
        PassInputField(Modifier, formData.password, "password", false, isPasswordVisible)
        Spacer(modifier = Modifier.height(80.dp))
        OnSubmitFormButton(navController, loginScreenViewModel, formData)
        Spacer(modifier = Modifier.height(40.dp))
        SwitchTOSignUpButton(navController)
    }
}

@Composable
fun OnSubmitFormButton(
    navController: NavController,
    loginScreenViewModel: LoginScreenViewModel,
    formData: UserLoginFormData
) {
    val context = LocalContext.current
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = NewWhiteFontColor,
            disabledContentColor = Color.Gray
        ),
        modifier = Modifier.background(MainButtonColor),
        onClick = {
            loginScreenViewModel.logUserIn(formData.username.value.trim(), formData.password.value.trim(),
                onSuccess = {
                    navController.navigate(AvailableScreens.GroupsScreen.name)
                },
                onError = {
                    Toast.makeText(context, "e-mail and/or password not matching", Toast.LENGTH_SHORT).show()
                }
            )
        }) {
        Text(text = "Submit", Modifier.background(Color.Transparent))
    }
}

@Composable
fun SwitchTOSignUpButton(navController: NavController) {
    Button(colors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = NewWhiteFontColor,
        disabledContentColor = Color.Gray,
    ),
        modifier = Modifier
            .background(Color(0f, 0f, 0f, alpha = 0f))
            .border(
                width = 2.dp,
                color = NewWhiteFontColor,
                shape = RoundedCornerShape(8.dp)
            ),
        onClick = { navController.navigate(AvailableScreens.SignUpScreen.name) }) {
        Text(color = Color.White, text = "Register")
    }
}



