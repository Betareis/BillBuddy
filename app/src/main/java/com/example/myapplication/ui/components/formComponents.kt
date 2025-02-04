package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmailInputField(
    modifier: Modifier,
    emailState: MutableState<String>,
    label: String = "email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default

) {
    TextField(
        label = { Text("Enter e-mail*") },
        value = emailState.value,
        onValueChange = { emailState.value = it },
        textStyle = TextStyle(fontSize = 15.sp, color = Color.Black),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.Transparent),
        keyboardActions = onAction,
    )
}

@Composable
fun SimpleTextInputField(
    modifier: Modifier,
    valueState: MutableState<String>,
    label: String,
    imeAction: ImeAction = ImeAction.Next,
) {
    TextField(
        label = { Text(label) },
        value = valueState.value,
        onValueChange = { valueState.value = it },
        textStyle = TextStyle(fontSize = 15.sp, color = Color.Black),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.Transparent),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = imeAction
        ),
    )
}


@Composable
fun PassInputField(
    modifier: Modifier,
    valueState: MutableState<String>,
    label: String,
    enabled: Boolean,
    isPasswordVisible: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default

) {
    val visualTransformation =
        if (!isPasswordVisible.value) PasswordVisualTransformation() else VisualTransformation.None
    TextField(
        label = { Text("Enter password*") },
        value = valueState.value,
        onValueChange = { valueState.value = it },
        textStyle = TextStyle(fontSize = 15.sp, color = Color.Black),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.Transparent),
        visualTransformation = visualTransformation,
        trailingIcon = { IsPasswordVisibleIcon(isPasswordVisible = isPasswordVisible) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
    )
}

@Composable
fun IsPasswordVisibleIcon(isPasswordVisible: MutableState<Boolean>) {
    IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
        Icon(imageVector = Icons.Rounded.Lock, contentDescription = "Password Visibility")
    }
}

