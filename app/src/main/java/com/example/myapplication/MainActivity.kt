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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
            HandleDeepLink(navController)
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppNavigation(PaddingValues(0.dp), navController)
                }
            }
        }
    }

    @Composable
    private fun HandleDeepLink(navController: NavController) {
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            val intent = (context as MainActivity).intent
            val data: Uri? = intent.data
            if (data != null) {
                val groupId = data.lastPathSegment
                if (groupId != null) {
                    navController.navigate("${AvailableScreens.TransactionsBalancesLayout.name}/?groupId=$groupId&groupName=$groupId")
                }
            }
        }
    }
}
