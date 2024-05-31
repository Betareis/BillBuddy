package com.example.myapplication.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.screens.LoginScreen
import com.example.myapplication.ui.screens.BalancesScreen
import com.example.myapplication.ui.screens.EditTransactionScreen
import com.example.myapplication.ui.screens.groups.GroupsScreen
import com.example.myapplication.ui.screens.NewEntryScreen
import com.example.myapplication.ui.screens.ProfileScreen
import com.example.myapplication.ui.screens.SignUpScreen
import com.example.myapplication.ui.screens.transactions.TransactionsScreen
import com.example.myapplication.ui.screens.TransactionInfoScreen

@Composable
fun AppNavigation(innerPaddingValues: PaddingValues) {
    val navController: NavHostController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AvailableScreens.LoginScreen.name,
        modifier = Modifier.padding(innerPaddingValues)
    )
    {
        composable(AvailableScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }
        composable(AvailableScreens.BalancesScreen.name) {
            BalancesScreen(navController)
        }
        composable(AvailableScreens.EditTransactionScreen.name) {
            EditTransactionScreen(navController)
        }
        composable(AvailableScreens.GroupsScreen.name) {
            GroupsScreen(navController)
        }
        composable(AvailableScreens.NewEntryScreen.name) {
            NewEntryScreen(navController)
        }
        composable(AvailableScreens.ProfileScreen.name) {
            ProfileScreen(navController)
        }
        composable(AvailableScreens.SignUpScreen.name) {
            SignUpScreen(navController)
        }
        /*composable(AvailableScreens.TransactionsScreen.name) {
            TransactionsScreen(navController, "bla")
        }*/

        composable(
            "${AvailableScreens.TransactionsScreen.name}/?groupName={groupName}",
            arguments = listOf(
                navArgument("groupName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupName")
            //val groupId = Uri.decode(encodedGroupName)
            if (groupId != null) {
                TransactionsScreen(navController, groupId)
            }
        }

        composable(AvailableScreens.TransactionInfoScreen.name) {
            TransactionInfoScreen(navController)
        }
    }
}

