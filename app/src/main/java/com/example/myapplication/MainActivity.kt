package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.navigation.AppNavigation
import com.example.myapplication.ui.navigation.AvailableScreens
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            // Handle Deep Link
            val intent = intent
            val deepLinkData = intent?.data
            if (deepLinkData != null) {
                handleDeepLink(navController, deepLinkData)
            }

            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppNavigation(PaddingValues(0.dp))
                }
            }
        }
    }

    private fun handleDeepLink(navController: NavController, deepLinkData: Uri) {
        val pathSegments = deepLinkData.pathSegments
        if (pathSegments.size == 2 && pathSegments[0] == "transactionscreen") {
            val groupName = pathSegments[1]
            val groupId = ""
            navController.navigate("${AvailableScreens.TransactionsScreen.name}/?groupId=${groupId}&groupName=${groupName}")
        }
    }
}
