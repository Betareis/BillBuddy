package com.example.myapplication.ui.screens.signup

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.ui.navigation.AvailableScreens

import com.example.myapplication.ui.theme.MainButtonColor;
import com.example.myapplication.ui.theme.NewWhiteFontColor;
import com.example.myapplication.ui.theme.ScreenBackgroundColor;


import com.example.myapplication.ui.components.EmailInputField
import com.example.myapplication.ui.components.PassInputField
import com.example.myapplication.ui.components.SimpleTextInputField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class UserRegisterFormData(
    val username: MutableState<String>,
    val firstname: MutableState<String>,
    val name: MutableState<String>,
    val password: MutableState<String>,
)

@Composable
fun SignUpScreen(navController: NavController, signUpViewModel: SignUpViewModel = hiltViewModel()) {
    val isPasswordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val formData by remember {
        mutableStateOf(
            UserRegisterFormData(
                mutableStateOf(""), mutableStateOf(""), mutableStateOf(""), mutableStateOf("")
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
            text = "SignUp",
            fontSize = 40.sp,
            color = NewWhiteFontColor,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            EmailInputField(Modifier, formData.username)
            SimpleTextInputField(Modifier, formData.firstname, "Enter firstname*")
            SimpleTextInputField(Modifier, formData.name, "Enter name*")
            PassInputField(Modifier, formData.password, "Enter password*", false, isPasswordVisible)
            Spacer(modifier = Modifier.height(80.dp))
        }
        OnSubmitFormButtonSignUp(navController, signUpViewModel, formData)
        Spacer(modifier = Modifier.height(40.dp))
        SwitchTOLoginButtonSignUp(navController);
    }
}

@Composable
fun OnSubmitFormButtonSignUp(
    navController: NavController, signUpViewModel: SignUpViewModel, formData: UserRegisterFormData
) {
    Button(colors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = NewWhiteFontColor,
        disabledContentColor = Color.Gray
    ), modifier = Modifier.background(MainButtonColor), onClick = {
        CoroutineScope(Dispatchers.Main).launch {
            signUpViewModel.registerUser(mapOf(
                "email" to formData.username.value,
                "firstname" to formData.firstname.value,
                "name" to formData.name.value,
                "password" to formData.password.value
            ),
            onSuccess = {
                navController.navigate(AvailableScreens.LoginScreen.name)
            })
        }

    }) {
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
        modifier = Modifier
            .background(Color(0f, 0f, 0f, alpha = 0f))
            .border(
                width = 2.dp,
                color = NewWhiteFontColor,
                shape = RoundedCornerShape(8.dp)
            ),
        onClick = { navController.navigate(AvailableScreens.LoginScreen.name) }) {
        Text(color = Color.White, text = "Back To Login")
    }
}



