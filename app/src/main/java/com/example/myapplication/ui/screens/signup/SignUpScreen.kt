package com.example.myapplication.ui.screens.signup
//import androidx.compose.foundation.background
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.ui.navigation.AvailableScreens

import com.example.myapplication.ui.theme.MainButtonColor;
import com.example.myapplication.ui.theme.NewWhiteFontColor;
import com.example.myapplication.ui.theme.ScreenBackgroundColor;


import com.example.myapplication.ui.components.EmailInputField
import com.example.myapplication.ui.components.PassInputField

data class UserRegisterFormData(
    val username: MutableState<String>,
    val password: MutableState<String>,
)


@Composable
fun SignUpScreen(navController: NavController) {
    val isPasswordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val formData by remember {
        mutableStateOf(
            UserRegisterFormData(
                mutableStateOf(""),
                mutableStateOf("")
            )
        )
    }

    Column(modifier = Modifier.padding(3.dp).background(ScreenBackgroundColor).fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "SignUp", fontSize = 40.sp, color = NewWhiteFontColor, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(40.dp))
        Spacer(modifier = Modifier.height(80.dp))
        EmailInputField(Modifier, formData.username)
        //UsernameTextFieldSignUp()
        Spacer(modifier = Modifier.height(40.dp))
        PassInputField(Modifier, formData.password, "password", false, isPasswordVisible)
        //PasswordTextFieldSignUp()
        Spacer(modifier = Modifier.height(80.dp))
        OnSubmitFormButtonSignUp(navController)
        Spacer(modifier = Modifier.height(40.dp))
        SwitchTOLoginButtonSignUp(navController);
    }
}


@Composable
fun UsernameTextFieldSignUp() {
    var username by rememberSaveable { mutableStateOf("") }

    TextField(
        value = username,
        onValueChange = { username = it },
        label = { Text("Enter username") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
fun PasswordTextFieldSignUp() {
    var password by rememberSaveable { mutableStateOf("") }
    TextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Enter password") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

@Composable
fun OnSubmitFormButtonSignUp(navController: NavController) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = NewWhiteFontColor,
            disabledContentColor = Color.Gray),
        modifier = Modifier.background(MainButtonColor),
        onClick = { navController.navigate(AvailableScreens.GroupsScreen.name) }) {
        Text(text = "Submit", Modifier.background(Color.Transparent))
    }
}

@Composable
fun SwitchTOLoginButtonSignUp(navController: NavController) {
    Button(colors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = NewWhiteFontColor,
        disabledContentColor = Color.Gray,
    ),
        modifier = Modifier.background(Color(0f, 0f, 0f, alpha = 0f)).border( // Add border with desired properties
            width = 2.dp,
            color = NewWhiteFontColor,
            shape = RoundedCornerShape(8.dp) // Example: Rounded corners
        ),
        onClick = { navController.navigate(AvailableScreens.LoginScreen.name) }) {
        Text(color = Color.White, text = "Login")
    }
}



