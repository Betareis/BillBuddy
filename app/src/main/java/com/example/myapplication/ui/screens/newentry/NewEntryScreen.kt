package com.example.myapplication.ui.screens.newentry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.example.myapplication.data.model.Transaction
import com.example.myapplication.ui.navigation.AvailableScreens
import com.example.myapplication.ui.screens.groups.GroupsViewModel
import com.example.myapplication.ui.theme.ScreenBackgroundColor
import com.example.myapplication.ui.theme.MainButtonColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.System.out
import java.time.Instant
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEntryScreen(
    navController: NavController,
    groupId: String,
    newEntryViewModel: NewEntryScreenViewModel = hiltViewModel()
) {
    var name by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }

    var exceptionMessage by rememberSaveable { mutableStateOf("") }

    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxSize()
            .background(
                ScreenBackgroundColor
            )
    ) {
        Column(
            modifier = Modifier
                .padding(3.dp)
        ) {
            Text(text = exceptionMessage, color = Color.White)
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

            /*val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = Instant.now().toEpochMilli()
            )*/


            val datePickerState =
                rememberDatePickerState(
                    initialDisplayedMonthMillis = System.currentTimeMillis(),
                    yearRange = 2000..2024
                )
            val showDatePicker = remember { mutableStateOf(false) }


            Button(onClick = { showDatePicker.value = false}) {
                Text(text = "Select Date")
            }
            val selectedDate = datePickerState.selectedDateMillis?.let {
                Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC)
            }


            /*val selectedDate =
                remember { mutableStateOf("") } // this will store whatever date the user selects*/
            if (showDatePicker.value) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker.value = false },
                    confirmButton = {
                        TextButton(
                            onClick = { showDatePicker.value = false },
                            enabled = datePickerState.selectedDateMillis != null
                        ) {
                            Text(text = "Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker.value = false }) {
                            Text(text = "Dismiss")
                        }
                    }) {
                    //Setting the selected date
                    //selectedDate.value = datePickerState.selectedDateMillis.toString()
                    DatePicker(state = datePickerState)
                }
            }

            /*DatePicker(
                colors = DatePickerDefaults.colors(
                    containerColor = Color.Black,
                    titleContentColor = Color.LightGray,
                    headlineContentColor = Color.LightGray,
                    weekdayContentColor = Color.LightGray,
                    subheadContentColor = Color.LightGray,
                    yearContentColor = Color.White,
                    currentYearContentColor = Color.White,
                    selectedYearContentColor = Color.Black,
                    selectedYearContainerColor = MainButtonColor,
                    dayContentColor = Color.White,
                    disabledDayContentColor = Color.LightGray,
                    selectedDayContentColor = Color.White,
                    disabledSelectedDayContentColor = Color.White,
                    selectedDayContainerColor = MainButtonColor,
                    disabledSelectedDayContainerColor = Color.LightGray,
                    todayContentColor = Color.LightGray,
                    todayDateBorderColor = Color.LightGray,
                    dayInSelectionRangeContentColor = Color.LightGray,
                    dayInSelectionRangeContainerColor = Color.LightGray,
                ),
                state = datePickerState
            )*/

            /*val selectedDate = datePickerState.selectedDateMillis?.let {
                Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC)
            }*/

// Finally, to get the user value you could do something like this:
            Text(
                color = Color.White,
                text = "Selected: ${selectedDate?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "no selection"}"
            )
            Text(color = Color.White, text = name)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                modifier = Modifier
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

            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 200.dp)
            ) {
                IconButton(
                    modifier = Modifier
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
                                    "amount" to amount
                                )
                            )

                            if (result.exception != null) {
                                exceptionMessage =
                                    result.exception!!.message.toString()
                            } else navController.popBackStack()


                            /*if (isDouble(amount)){
                                /*val flatTransactionData = mapOf(
                                    "amount" to amount.toDouble(),
                                    "name" to name
                                )*/
                                //Todo: Not working right now
                                val result = newEntryViewModel.addTransactionForGroup(groupId, flatTransactionData)

                                if (result.exception != null){
                                    exceptionMessage = result.exception!!.message.toString()
                                }
                                }
                             */
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add a Group" // Provide a description for accessibility
                    )
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
            Text(text = "For: ", color = Color.White, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(50.dp))
            LazyColumn {
                /*
                * Todo: List of the total expense of each involved group members with quantity
                * */

            }

        }


    }
}
