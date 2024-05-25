package com.example.myapplication.ui.screens
//import androidx.compose.foundation.background
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
//import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.ui.navigation.AvailableScreens

data class UserLoginFormData(
    val username: MutableState<String>,
    val password: MutableState<String>,
)


@Composable
fun LoginScreen(navController: NavController) {
    val formData by remember {
        mutableStateOf(
            UserLoginFormData(
                mutableStateOf(""),
                mutableStateOf("")
            )
        )
    }

    Column(modifier = Modifier.padding(3.dp)) {
        Text(text = "Login", fontSize = 40.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(40.dp))
        SwitchTOSignUpButton(navController);
        Spacer(modifier = Modifier.height(80.dp))
        UsernameTextField()
        Spacer(modifier = Modifier.height(40.dp))
        PasswordTextField()
        Spacer(modifier = Modifier.height(80.dp))
        OnSubmitFormButton(navController)

    }
    //value = ,
    //, onValueChange

    //Text(text = "test", color = Color.Blue)
    //}
}


@Composable
fun UsernameTextField() {
    var username by rememberSaveable { mutableStateOf("") }

    TextField(
        value = username,
        onValueChange = { username = it },
        label = { Text("Enter username") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
fun PasswordTextField() {
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
fun OnSubmitFormButton(navController: NavController) {
    Button(onClick = { navController.navigate(AvailableScreens.GroupsScreen.name) }) {
        Text(text = "Submit")
    }
}

@Composable
fun SwitchTOSignUpButton(navController: NavController) {
    Button(modifier = Modifier.background(Color(0f,0f,0f, alpha = 0f)), onClick = { navController.navigate(AvailableScreens.SignUpScreen.name) }) {
        Text(color = Color.White, text = "Register")
    }
}



