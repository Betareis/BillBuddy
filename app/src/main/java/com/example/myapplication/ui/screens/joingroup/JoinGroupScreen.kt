package com.example.myapplication.ui.screens.joingroup

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.data.repository.FirestoreRepository
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.ui.navigation.AvailableScreens
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.format.DateTimeFormatter

@Composable
fun JoinGroupScreen(
    navController: NavController,
    groupId: String,
    uid: String,
) {
    Column {
        Text(text = "GroupId: $groupId", color = Color.Black)
        Text(text = "UserId:$uid", color = Color.Black)
        var showTextFieldDialog by remember { mutableStateOf(false) }
        var exceptionMessage by remember { mutableStateOf("") }

        if (showTextFieldDialog) {
            AlertDialog(text = { Text(text = exceptionMessage) },
                onDismissRequest = { showTextFieldDialog = false },
                confirmButton = { showTextFieldDialog = false })
        }
        Button(onClick = {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val firestore = FirebaseFirestore.getInstance()
                    val userDocumentRef = firestore.collection("users").document(uid)
                    val groupDocumentRef = firestore.collection("groups").document(groupId)
                    if (groupDocumentRef.get().await().exists() && userDocumentRef.get().await()
                            .exists()
                    ) {
                        userDocumentRef.update("groups", FieldValue.arrayUnion(groupId))
                        groupDocumentRef.update("users", FieldValue.arrayUnion(uid))
                    } else throw Exception("Failed to get the data |join Group|")

                } catch (e: Exception) {
                    exceptionMessage = e.message.toString()
                }
                /*val process = joinGroupViewModel.addUserToGroup(uid, groupId)

                if (process.exception != null) {
                    exceptionMessage = process.exception!!.message.toString()

                } else {
                    navController.navigate(AvailableScreens.GroupsScreen.name)
                }*/
            }
        }) {
            Text(text = "Join Group")
        }
    }
}