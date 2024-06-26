package com.example.myapplication.ui.screens.newentry

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.data.model.User
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.ui.navigation.AvailableScreens
import com.example.myapplication.ui.theme.ScreenBackgroundColor
import com.example.myapplication.ui.theme.MainButtonColor
import com.example.myapplication.ui.theme.NewWhiteFontColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun isDouble(value: String): Boolean {
    return try {
        if (value.isEmpty()) false
        else {
            value.toDouble()
            true
        }
    } catch (e: NumberFormatException) {
        false
    }
}

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEntryScreen(
    navController: NavController,
    groupId: String,
    newEntryViewModel: NewEntryScreenViewModel = hiltViewModel()
) {
    var name by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }

    val selectedUserId by rememberSaveable {
        mutableStateOf(
            mutableStateOf("")
        )
    }

    val fieldValues = remember {
        mutableStateOf(mutableMapOf<String, Double>())
    }

    val datePickerState = rememberDatePickerState(
        initialDisplayedMonthMillis = System.currentTimeMillis(), yearRange = 2000..2024
    )

    val selectedDate = datePickerState.selectedDateMillis?.let {
        Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC)
    }

    val exceptionMessage by rememberSaveable { mutableStateOf(mutableStateOf("")) }

    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(3.dp)
            ) {
                Text(text = exceptionMessage.value, color = Color.White)
                OutlinedTextField(
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    value = amount,
                    onValueChange = { newValue ->
                        amount = newValue
                        if (isDouble(newValue)) {
                            val numUsers = fieldValues.value.size
                            val newAmount = newValue.toDouble()
                            val newFieldValues = fieldValues.value.mapValues {

                                Math.round((newAmount / numUsers) * 100.0) / 100.0
                            }.toMutableMap()
                            fieldValues.value = newFieldValues
                        }
                    },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                )
                Spacer(modifier = Modifier.height(10.dp))

                val showDatePicker = remember { mutableStateOf(false) }

                Button(
                    onClick = { showDatePicker.value = true },
                    colors = ButtonDefaults.buttonColors(MainButtonColor)
                ) {
                    Text(text = "Select Date", color = Color.Black)
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    if (showDatePicker.value) {
                        DatePickerDialog(onDismissRequest = { showDatePicker.value = false },
                            confirmButton = {
                                TextButton(
                                    onClick = { showDatePicker.value = false }, enabled = true
                                ) {
                                    Text(text = "Confirm")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDatePicker.value = false }) {
                                    Text(text = "Dismiss")
                                }
                            }) {
                            DatePicker(state = datePickerState)
                        }
                    }
                    TextField(value = selectedDate?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "",
                        modifier = Modifier.width(150.dp),
                        enabled = false,
                        onValueChange = {})
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Payed by:", color = Color.White, fontSize = 20.sp)
                DropdownPayedByUser(
                    loadUsers = { newEntryViewModel.getUsersOfGroup(groupId) }, selectedUserId
                )

                Spacer(modifier = Modifier.height(30.dp))
                Text(text = "For: ", color = Color.White, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(20.dp))
                SingleAmountMembers(
                    loadUsers = { newEntryViewModel.getUsersOfGroup(groupId) },
                    amount,
                    fieldValues.value
                )
                Spacer(modifier = Modifier.height(50.dp))
            }
            Box(
                modifier = Modifier
                    //.fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                AddTransactionButtonView(
                    navController,
                    newEntryViewModel,
                    groupId,
                    name,
                    amount,
                    selectedUserId,
                    selectedDate,
                    fieldValues.value,
                    exceptionMessage
                )
            }
        }
    }
}

@Composable
fun AddTransactionButtonView(
    navController: NavController,
    newEntryViewModel: NewEntryScreenViewModel,
    groupId: String,
    name: String,
    amount: String,
    selectedUserId: MutableState<String>,
    selectedDate: OffsetDateTime?,
    fieldValues: MutableMap<String, Double>,
    exceptionMessage: MutableState<String>,
) {
    val context = LocalContext.current
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    val result = newEntryViewModel.addTransactionForGroup(
                        groupId, mapOf(
                            "name" to name,
                            "amount" to amount,
                            "payedBy" to selectedUserId.value,
                            "date" to if (selectedDate != null) selectedDate.format(
                                DateTimeFormatter.ISO_LOCAL_DATE
                            ) else "",
                        ), fieldValues.toMap()
                    )

                    if (result.exception != null) {
                        val entryfailed = result.exception!!.message.toString()
                        Toast.makeText(context, entryfailed, Toast.LENGTH_SHORT).show()
                    } else {
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier
                .size(50.dp)
                .padding(start = 30.dp)
                .background(MainButtonColor)
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Add a transaction",
            )
        }
    }
}


@SuppressLint("MutableCollectionMutableState")
@Composable
fun SingleAmountMembers(
    loadUsers: suspend () -> DataRequestWrapper<MutableList<User>, String, Exception>,
    amount: String,
    fieldValues: MutableMap<String, Double>
) {
    val userListData = produceState<DataRequestWrapper<MutableList<User>, String, Exception>>(
        initialValue = DataRequestWrapper(state = "loading")
    ) {
        value = loadUsers()
    }.value

    if (userListData.state == "loading") {
        Text(text = "Users loading")
        CircularProgressIndicator()
    } else if (userListData.data != null && userListData.data!!.isNotEmpty()) {
        val numUsers = userListData.data!!.size
        if (isDouble(amount) && amount.toDouble() > 0.0) {
            val newFieldValues = userListData.data!!.associateBy({ it.id.toString() }, {
                Math.round((amount.toDouble() / numUsers) * 100.0) / 100.0
            }).toMutableMap()
            fieldValues.clear()
            fieldValues.putAll(newFieldValues)
            Log.d("TestFieldValues", fieldValues.toString())
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 70.dp)
        ) {
            for (user in userListData.data!!) item() {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        color = Color.White,
                        text = user.getDisplayName(),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.weight(0.2f))
                    TextField(
                        readOnly = true,
                        value = fieldValues.getOrElse(user.id as String) { 0.0 }.toString(),
                        onValueChange = { newValue ->
                            fieldValues.toMutableMap().apply { put(user.id, newValue.toDouble()) }
                        },
                        modifier = Modifier.weight(2f),
                    )
                }
            }
        }
    }
}

@Composable
fun DropdownPayedByUser(
    loadUsers: suspend () -> DataRequestWrapper<MutableList<User>, String, Exception>,
    selectedUserId: MutableState<String>
) {
    var expanded by remember { mutableStateOf(false) }

    val userListData = produceState<DataRequestWrapper<MutableList<User>, String, Exception>>(
        initialValue = DataRequestWrapper(state = "loading")
    ) {
        value = loadUsers()
    }.value

    if (userListData.state == "loading") {
        Text(text = "Users for dropdown loading")
        CircularProgressIndicator()
    } else if (userListData.data != null && userListData.data!!.isNotEmpty()) {
        Log.d("user_list", userListData.data.toString())
        val nameList = mutableListOf<String>()
        for (user in userListData.data!!) {
            // Add the user's name to the nameList
            nameList.add(user.getDisplayName())
        }

        var selectedIndex by remember { mutableIntStateOf(0) }

        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart)
                .height(20.dp)
        ) {
            Text(
                if (selectedUserId.value.isNotEmpty()) {
                    // Display the selected user's name instead of ID
                    userListData.data!!.find { it.id == selectedUserId.value }?.getDisplayName()
                        ?: "Select a user"
                } else {
                    "Select a user"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clickable(onClick = { expanded = true })
                    .background(
                        NewWhiteFontColor
                    )
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        NewWhiteFontColor
                    )
            ) {
                nameList.forEachIndexed { index, displayName ->
                    DropdownMenuItem(text = { Text(text = displayName) }, onClick = {
                        selectedUserId.value = userListData.data!![index].id.toString()
                        selectedIndex = index
                        expanded = false
                    })
                }
            }
        }
    }
}