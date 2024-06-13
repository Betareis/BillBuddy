package com.example.myapplication.ui.navigation

import android.util.Log
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
import com.example.myapplication.ui.screens.login.LoginScreen
import com.example.myapplication.ui.screens.BalancesScreen
import com.example.myapplication.ui.screens.EditTransactionScreen
import com.example.myapplication.ui.screens.groups.GroupsScreen
import com.example.myapplication.ui.screens.newentry.NewEntryScreen
import com.example.myapplication.ui.screens.ProfileScreen
import com.example.myapplication.ui.screens.signup.SignUpScreen
import com.example.myapplication.ui.screens.transactions.TransactionsScreen
import com.example.myapplication.ui.screens.TransactionInfoScreen

@Composable
fun AppNavigation(innerPaddingValues: PaddingValues) {
    val navController: NavHostController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AvailableScreens.GroupsScreen.name,
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

        composable(
            "${AvailableScreens.NewEntryScreen.name}/?groupId={groupId}",
            arguments = listOf(
                navArgument("groupId") {
                    type = NavType.StringType
                },
            )
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")

            Log.d("NavigationArgs", "groupId $groupId")

            if (groupId != null) {
                NewEntryScreen(navController, groupId)
            }
        }


        /*composable(AvailableScreens.NewEntryScreen.name) {
            NewEntryScreen(navController)
        }*/
        composable(AvailableScreens.ProfileScreen.name) {
            ProfileScreen(navController)
        }
        composable(AvailableScreens.SignUpScreen.name) {
            SignUpScreen(navController)
        }

        //Todo: Should be remove in production
        /*composable(AvailableScreens.TransactionsScreen.name) {
            TransactionsScreen(navController, "bla")
        }*/

        composable(
            "${AvailableScreens.TransactionsScreen.name}/?groupId={groupId}&groupName={groupName}",
            arguments = listOf(
                navArgument("groupId") {
                    type = NavType.StringType
                },
                navArgument("groupName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")
            val groupName = backStackEntry.arguments?.getString("groupName")

            Log.d("NavigationArgs", "groupId: $groupId, groupName: $groupName")

            if (groupId != null && groupName != null) {
                TransactionsScreen(navController, groupId, groupName)
            }
        }

        composable(AvailableScreens.TransactionInfoScreen.name) {
            TransactionInfoScreen(navController)
        }
    }
}

