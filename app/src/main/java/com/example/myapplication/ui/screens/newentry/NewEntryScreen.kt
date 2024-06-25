package com.example.myapplication.ui.screens.newentry

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun isDouble(value: String): Boolean {
    return try {
        value.toDouble()
        true
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

    val fieldValues by rememberSaveable {
        mutableStateOf(
            mutableMapOf<String, Double>()
        )
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
            .background(
                ScreenBackgroundColor
            )
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
                onValueChange = { amount = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            )
            Spacer(modifier = Modifier.height(10.dp))

            val showDatePicker = remember { mutableStateOf(false) }


            Button(onClick = { showDatePicker.value = true }) {
                Text(text = "Select Date")
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
            }/*Text(
                color = Color.White,
                text = "Selected: ${selectedDate?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "no selection"}"
            )*/
            DropdownPayedByUser(
                loadUsers = { newEntryViewModel.getUsersOfGroup(groupId) }, selectedUserId
            )

            //Text(color = Color.White, text = name)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(modifier = Modifier
                .then(Modifier.testTag("backArrow"))
                .width(200.dp),
                onClick = {
                    navController.navigate(AvailableScreens.GroupsScreen.name)
                }) {
                Row {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "AddImage",
                        tint = Color.LightGray
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        color = Color.LightGray,
                        text = "File upload",
                        modifier = Modifier.width(400.dp),
                        fontSize = 20.sp
                    )
                }
            }
            AddTransactionButtonView(
                navController,
                newEntryViewModel,
                groupId,
                name,
                amount,
                selectedUserId,
                selectedDate,
                fieldValues,
                exceptionMessage
            )

            Spacer(modifier = Modifier.height(50.dp))
            Text(text = "For: ", color = Color.White, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(50.dp))
            SingleAmountMembers(
                loadUsers = { newEntryViewModel.getUsersOfGroup(groupId) }, amount, fieldValues
            )
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
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 10.dp)
    ) {
        IconButton(modifier = Modifier
            .padding(start = 15.dp, end = 15.dp)
            .fillMaxWidth()
            .width(300.dp)
            .size(50.dp) // Adjust size as needed
            .background(MainButtonColor),// Set background color to blue
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    val result = newEntryViewModel.addTransactionForGroup(
                        groupId, mapOf(
                            "name" to name,
                            "amount" to amount,
                            "payedBy" to selectedUserId.value,
                            "date" to if (selectedDate !== null) selectedDate.format(
                                DateTimeFormatter.ISO_LOCAL_DATE
                            ) else "",
                        ), fieldValues.toMap()
                    )

                    if (result.exception != null) {
                        exceptionMessage.value = result.exception!!.message.toString()
                    } else navController.popBackStack()
                }
            }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add a Group" // Provide a description for accessibility
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
        val readOnlyList = userListData.data!!.toList()
        val myMap: Map<String, Double> =
            readOnlyList.associateBy({ it.id as String }, // Key extractor: extracts user ID as string
                {
                    val formattedAmount = amount.replace(",", ".")
                    if (formattedAmount.isNotEmpty() && isDouble(formattedAmount) && formattedAmount.toDouble() > 0.0) {
                        String.format("%.2f", formattedAmount.toDouble() / numUsers)
                            .toDouble() // Format and convert to double
                    } else {
                        0.0  // Default value as double
                    }
                })
        fieldValues.putAll(myMap)

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
                        value = fieldValues.getOrElse(user.id as String) { 0.0 }.toString().replace(".", ","),
                        onValueChange = { newValue ->
                            val formattedValue = newValue.replace(",", ".")
                            fieldValues.toMutableMap().apply { put(user.id, formattedValue.toDouble()) }
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

        val items = nameList.toList()
        val disabledValue = "B"
        var selectedIndex by remember { mutableIntStateOf(0) }

        Text(text = selectedUserId.value, color = Color.White)
        Box(
            modifier = Modifier
                //.fillMaxSize()
                .wrapContentSize(Alignment.TopStart)
                .height(20.dp)
        ) {
            Text(
                items[selectedIndex],
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clickable(onClick = { expanded = true })
                    .background(
                        MainButtonColor
                    )
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MainButtonColor
                    )
            ) {
                nameList.forEachIndexed { index, s ->
                    DropdownMenuItem(text = { Text(text = s) }, onClick = {
                        selectedUserId.value = userListData.data!![index].id.toString()
                        selectedIndex = index
                        expanded = false
                    })
                }
            }
        }
    }
}


