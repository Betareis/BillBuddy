package com.example.myapplication.ui.screens.groups

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.data.model.Group
import com.example.myapplication.ui.navigation.AvailableScreens
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.ui.navigation.TabView
import com.example.myapplication.ui.theme.NewWhiteFontColor
import com.example.myapplication.ui.theme.ListElementBackgroundColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.myapplication.ui.theme.MainButtonColor
import com.google.firebase.auth.FirebaseAuth

fun String.toUppercaseFirstLetter(): String {
    if (isEmpty()) return ""
    return first().uppercaseChar() + substring(1)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsScreen(navController: NavController, groupViewModel: GroupsViewModel = hiltViewModel()) {
    val groupsState = groupViewModel.groupsState.collectAsState(initial = emptyList())
    var showTextFieldDialog by remember { mutableStateOf(false) }
    var textFieldValue by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        groupViewModel.getGroupsFirestore()
    }

    Scaffold(contentColor = Color.Black, bottomBar = { TabView(navController) }, topBar = {
        CenterAlignedTopAppBar(title = { Text(text = "Groups") })
        //BurgerMenuDrawer()
    }

    ) {
        Surface(
            modifier = Modifier
                .padding(top = 60.dp)
                .fillMaxSize(),
            color = Color.White,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ShowData(
                    groupData = groupsState.value,
                    //loadGroups = { groupViewModel.getGroupsFirestore() },
                    navController = navController
                )
            }

            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 200.dp)
            ) {
                IconButton(modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp)
                    .fillMaxWidth()
                    .width(300.dp)
                    .size(50.dp) // Adjust size as needed
                    .background(MainButtonColor),// Set background color to blue
                    onClick = {
                        showTextFieldDialog = true/*CoroutineScope(Dispatchers.Main).launch {
                            val flatGroupData = mapOf(
                                "name" to "Test",
                            )
                            //Todo: Not working right now
                            val auth = FirebaseAuth.getInstance()

                            groupViewModel.addGroup(flatGroupData, userId = auth.uid!!)

                            groupViewModel.getGroupsFirestore()
                        }*/
                    }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add a Group", // Provide a description for accessibility
                        tint = Color.Black
                    )
                }
            }
            if (showTextFieldDialog) {
                ShowTextFieldDialog(title = "Enter Text",
                    initialValue = textFieldValue,
                    onValueChange = { newText -> textFieldValue = newText },
                    onDismissRequest = {
                        CoroutineScope(Dispatchers.Main).launch {
                            if (textFieldValue.isNotBlank()) {
                                val flatGroupData = mapOf(
                                    "name" to textFieldValue,
                                )
                                //Todo: Not working right now
                                val auth = FirebaseAuth.getInstance()

                                groupViewModel.addGroup(flatGroupData, userId = auth.uid!!)

                                groupViewModel.getGroupsFirestore()
                            }
                        }
                        textFieldValue = "";
                        showTextFieldDialog = false;

                    })
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShowData(
    groupData: List<Group>, navController: NavController
) {
    if (groupData.isEmpty()) {
        Text(text = "No Groups found")
        //CircularProgressIndicator()
    } else {
        Log.d("DONE", "LOADING DATA DONE")
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .heightIn(max = 550.dp)
                    .fillMaxWidth()
            ) {
                for (data in groupData) {
                    item {
                        FilledTonalButton(
                            onClick = {
                                navController.navigate("${AvailableScreens.TransactionsBalancesLayout.name}/?groupId=${data.id}&groupName=${data.name}")
                            },
                            colors = ButtonColors(
                                contentColor = NewWhiteFontColor,
                                containerColor = ListElementBackgroundColor,
                                disabledContentColor = Color.LightGray,
                                disabledContainerColor = Color.LightGray
                            ),
                            modifier = Modifier
                                .padding(start = 15.dp, end = 15.dp)
                                .fillMaxWidth()
                                .align(alignment = Alignment.Center)
                                .requiredHeight(height = 60.dp)
                                .testTag("groupButton${data.name}"),
                            border = BorderStroke(1.dp, Color.Black),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 10.dp
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                (data.name).toUppercaseFirstLetter(),
                                color = NewWhiteFontColor,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowTextFieldDialog(
    title: String,
    onValueChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    initialValue: String = "",
) {
    AlertDialog(onDismissRequest = onDismissRequest, title = { Text(text = title) }, text = {
        TextField(
            value = initialValue, onValueChange = onValueChange, modifier = Modifier.fillMaxWidth()
        )
    }, confirmButton = {
        Button(onClick = onDismissRequest) {
            Text("Save") // Adjust button text as needed
        }
    })
}