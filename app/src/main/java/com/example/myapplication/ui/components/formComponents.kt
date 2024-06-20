package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
        label = { Text("Enter username") },
        value = emailState.value,
        onValueChange = { emailState.value = it },
        textStyle = TextStyle(fontSize = 15.sp, color = Color.Black),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.Transparent),
        //.heightIn(min = 80.dp),
        //.background(color = backgroundColor),
        //keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
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
        label = { Text("Enter password") },
        value = valueState.value,
        onValueChange = { valueState.value = it },
        textStyle = TextStyle(fontSize = 15.sp, color = Color.Black),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.Transparent),
        //.heightIn(min = 80.dp),
        visualTransformation = visualTransformation,
        trailingIcon = { isPasswordVisibleIcon(isPasswordVisible = isPasswordVisible) },
        //.background(color = backgroundColor),
        //keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
    )
}

@Composable
fun isPasswordVisibleIcon(isPasswordVisible: MutableState<Boolean>) {
    IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
        Icon(imageVector = Icons.Rounded.Lock, contentDescription = "Password Visibility")
    }
}

