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
fun EditTransactionForm(transactionName: String) {
    val transactionNameCopy = transactionName;
    var transaction_price by rememberSaveable { mutableStateOf("") }
    var transaction_date by rememberSaveable { mutableStateOf("") }
    var transaction_included_user by rememberSaveable { mutableStateOf("") }
    Column {
        /*TextField(
            readOnly = true,
            value = transactionNameCopy,
            //onValueChange = { transaction_name = it },
            //label = { Text("Enter new transaction name") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )*/
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = transaction_price,
            onValueChange = { transaction_price = it },
            label = { Text("Enter amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        TextField(
            value = transaction_date,
            onValueChange = { transaction_date = it },
            label = { Text("Enter date") },
            //Todo: Keyboard type should be date but it is not available
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        /*TextField(
            value = transaction_name,
            onValueChange = { transaction_name = it },
            label = { Text("Enter new transaction name") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )*/
    }
}

/*class SelectionOption(val option: String, var initialSelectedValue: Boolean) {
    var selected by mutableStateOf(initialSelectedValue)
}

private val _users = mutableListOf<User>(User("peter", 10.0), User("test", 20.5))

private val _options = listOf(
    SelectionOption("option 1", false),
    SelectionOption("option 2", false),
    SelectionOption("option 3", false),
    SelectionOption("option 4", false),
    SelectionOption("option 5", false)
).toMutableStateList()
val options: List<SelectionOption>
    get() = _options

@Composable
fun SingleSelectionList(options: List<SelectionOption>, onOptionClicked: (SelectionOption) -> Unit) {
    LazyColumn {
        items(options) { option -> SingleSelectionCard(option, onOptionClicked) }
    }
}

@Composable
fun SingleSelectionCard(selectionOption: SelectionOption, onOptionClicked: (SelectionOption) -> Unit) {
    Surface(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 8.dp, vertical = 4.dp)) {
        Surface(
            //modifier = Modifier
                //.border(1.dp, MaterialTheme.colors.primary, RectangleShape)
              //  .clickable(true, onClick = { onOptionClicked(selectionOption) }),
            //color = if (selectionOption.selected) { MaterialTheme.colors.primary } else { MaterialTheme.colors.background },
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = selectionOption.option,
                    //style = MaterialTheme.typography.body1
                )
            }
        }
    }
}*/

@Composable
fun EmailInputField(modifier: Modifier,
                    emailState: MutableState<String>,
                    label:String = "email",
                    enabled:Boolean = true,
                    imeAction: ImeAction = ImeAction.Next,
                    onAction: KeyboardActions = KeyboardActions.Default

){
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
fun PassInputField(modifier: Modifier,
                   valueState: MutableState<String>,
                   label: String,
                   enabled: Boolean,
                   isPasswordVisible: MutableState<Boolean>,
                   imeAction: ImeAction = ImeAction.Next,
                   onAction: KeyboardActions = KeyboardActions.Default

){
    val visualTransformation = if(!isPasswordVisible.value) PasswordVisualTransformation() else VisualTransformation.None
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
        visualTransformation =   visualTransformation,
        trailingIcon = {isPasswordVisibleIcon(isPasswordVisible = isPasswordVisible)} ,
        //.background(color = backgroundColor),
        //keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardOptions = KeyboardOptions(
            keyboardType= KeyboardType.Password,
            imeAction=imeAction),
    )
}
@Composable
fun isPasswordVisibleIcon(isPasswordVisible: MutableState<Boolean>) {
    IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
        Icon(imageVector = Icons.Rounded.Lock, contentDescription = "Password Visibility" )
    }
}

