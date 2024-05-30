package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screens.LoginScreen
import com.example.myapplication.ui.screens.BalancesScreen
import com.example.myapplication.ui.screens.EditTransactionScreen
import com.example.myapplication.ui.screens.groups.GroupsScreen
import com.example.myapplication.ui.screens.NewEntryScreen
import com.example.myapplication.ui.screens.ProfileScreen
import com.example.myapplication.ui.screens.SignUpScreen
import com.example.myapplication.ui.screens.TransactionsScreen
import com.example.myapplication.ui.screens.TransactionInfoScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AvailableScreens.ProfileScreen.name
    )
    {
        composable( AvailableScreens.LoginScreen.name){
            LoginScreen(navController = navController)
        }
        composable( AvailableScreens.BalancesScreen.name){
            BalancesScreen(navController)
        }
        composable( AvailableScreens.EditTransactionScreen.name){
            EditTransactionScreen(navController)
        }
        composable( AvailableScreens.GroupsScreen.name){
            GroupsScreen(navController)
        }
        composable( AvailableScreens.NewEntryScreen.name){
            NewEntryScreen(navController)
        }
        composable( AvailableScreens.ProfileScreen.name){
            ProfileScreen(navController)
        }
        composable( AvailableScreens.SignUpScreen.name){
            SignUpScreen(navController)
        }
        composable( AvailableScreens.TransactionsScreen.name){
            TransactionsScreen(navController)
        }
        composable( AvailableScreens.TransactionInfoScreen.name){
            TransactionInfoScreen(navController)
        }
    }
}