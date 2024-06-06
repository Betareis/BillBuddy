package com.example.myapplication.ui.screens.groups

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
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
import com.example.myapplication.data.model.User;
import com.example.myapplication.ui.navigation.AvailableScreens
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.ui.navigation.NavigationBar
import com.example.myapplication.ui.navigation.TabView
import com.example.myapplication.ui.theme.NewWhiteFontColor;
import com.example.myapplication.ui.theme.ListElementBackgroundColor;


fun String.toUppercaseFirstLetter(): String {
    if (isEmpty()) return ""
    return first().uppercaseChar() + substring(1)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsScreen(navController: NavController, groupViewModel: GroupsViewModel = hiltViewModel()) {

    Scaffold(
        contentColor = Color.Black,
        bottomBar = { TabView(navController) }


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
                    loadGroups = { groupViewModel.getGroupsFirestore() },
                    navController = navController
                )
            }

        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShowData(
    loadGroups: suspend () -> DataRequestWrapper<MutableList<Group>, String, Exception>,
    navController: NavController
) {
    val groupData = produceState<DataRequestWrapper<MutableList<Group>, String, Exception>>(
        initialValue = DataRequestWrapper(state = "loading")
    ) {
        value = loadGroups()
    }.value

    if (groupData.state == "loading") {
        Text(text = "Group screen")
        CircularProgressIndicator()
    } else if (groupData.data != null && groupData.data!!.isNotEmpty()) {
        Log.d("DONE", "LOADING DATA DONE")
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                for (data in groupData.data!!) {
                    item() {
                        FilledTonalButton(
                            onClick = {
                                navController.navigate("${AvailableScreens.TransactionsScreen.name}/?groupId=${data.id}&groupName=${data.name}")
                            },
                            colors = ButtonColors(contentColor = NewWhiteFontColor, containerColor = ListElementBackgroundColor, disabledContentColor = Color.LightGray, disabledContainerColor = Color.LightGray),
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
                            Text((data.name).toUppercaseFirstLetter(), color = NewWhiteFontColor, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
        Column(modifier = Modifier.padding(50.dp)) {
            Text(text = "Add groups icon")
        }

    } else {
        Text(text = "no groups found")
    }
}