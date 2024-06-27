package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.ui.navigation.AvailableScreens
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun JoinGroupScreen(
    navController: NavController,
    groupId: String,
    uid: String,
) {
    var groupName by remember { mutableStateOf<String?>(null) }
    var showTextFieldDialog by remember { mutableStateOf(false) }
    var exceptionMessage by remember { mutableStateOf("") }

    LaunchedEffect(groupId) {
        try {
            val firestore = FirebaseFirestore.getInstance()
            val groupDocumentRef = firestore.collection("groups").document(groupId)
            val groupDocument = groupDocumentRef.get().await()
            if (groupDocument.exists()) {
                groupName = groupDocument.getString("name")
            } else {
                exceptionMessage = "Group not found"
                showTextFieldDialog = true
            }
        } catch (e: Exception) {
            exceptionMessage = e.message.toString()
            showTextFieldDialog = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        groupName?.let { name ->
            Text(
                text = "Do you want to join: $name ?",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        } ?: run {
            Text(
                text = "Loading...",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (showTextFieldDialog) {
            AlertDialog(
                onDismissRequest = { showTextFieldDialog = false },
                text = { Text(text = exceptionMessage) },
                confirmButton = {
                    Button(onClick = { showTextFieldDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }

        Button(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        val firestore = FirebaseFirestore.getInstance()
                        val userDocumentRef = firestore.collection("users").document(uid)
                        val groupDocumentRef = firestore.collection("groups").document(groupId)
                        if (groupDocumentRef.get().await().exists() && userDocumentRef.get().await().exists()) {
                            userDocumentRef.update("groups", FieldValue.arrayUnion(groupId)).await()
                            groupDocumentRef.update("users", FieldValue.arrayUnion(uid)).await()
                            navController.navigate(AvailableScreens.GroupsScreen.name)
                        } else {
                            throw Exception("Failed to get the data |join Group|")
                        }
                    } catch (e: Exception) {
                        exceptionMessage = e.message.toString()
                        showTextFieldDialog = true
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Join Group")
        }
    }
}
