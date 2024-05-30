package com.example.myapplication.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.data.model.DCGroup
import com.example.myapplication.data.model.DCTransaction
import com.example.myapplication.data.model.User
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.navigation.AvailableScreens

val _users = listOf<User>(User("peter", 10.0), User("test", 20.5)).toMutableList<User>()

val _transaction: MutableList<DCTransaction> = listOf(
    DCTransaction("", 200.4, _users, _users),
    DCTransaction("", .4, _users, _users),
    DCTransaction("", 200.4, _users, _users)
).toMutableList()

@Composable
fun GroupsScreen(navController: NavController) {
    var groups by remember { mutableStateOf(listOf(
        DCGroup("Austria", _transaction),
        DCGroup("Austria", _transaction),
        DCGroup("Austria", _transaction)
    ).toMutableList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Zentriere die Elemente horizontal
    ) {
        Spacer(modifier = Modifier.height(16.dp)) // Füge oben etwas Platz hinzu
        Text(
            text = "GroupsScreen",
            color = Color.Black,
            fontSize = 24.sp, // Optional: Größe des Textes anpassen
            modifier = Modifier.align(Alignment.CenterHorizontally) // Zentriere den Text
        )
        Spacer(modifier = Modifier.height(32.dp)) // Füge etwas Platz zwischen dem Titel und den Buttons hinzu
        for (group in groups) {
            FilledTonalButton(
                onClick = {
                    navController.navigate(AvailableScreens.TransactionsScreen.name)
                },
                colors = ButtonDefaults.filledTonalButtonColors(),
                modifier = Modifier
                    .padding(vertical = 8.dp) // Füge vertikales Padding zwischen den Buttons hinzu
                    .fillMaxWidth(0.85f) // Passe die Breite der Buttons an
                    .height(60.dp) // Höhe der Buttons
                    .testTag("groupButton${group.name}"),
                border = BorderStroke(1.dp, Color.Black),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 10.dp
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(group.name, color = Color.Black)
            }
        }
        Spacer(modifier = Modifier.weight(1f)) // Platzhalter, um den unteren Button nach unten zu schieben
        Button(
            onClick = {
                // Logik zum Hinzufügen einer neuen Gruppe
                groups = groups.toMutableList().apply { add(DCGroup("Neue Gruppe", _transaction)) }
            },
            //colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81D4FA)),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.2f) // Passe die Breite des Buttons an
                .height(50.dp) // Höhe des Buttons
        ) {
            Text("+", color = Color.White)
        }
    }
}
