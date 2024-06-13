package com.example.myapplication.ui.navigation

import com.example.myapplication.ui.screens.BalancesScreen


enum class AvailableScreens {
    //EntryLoadingScreen;
    LoginScreen,
    BalancesScreen,
    EditTransactionScreen,
    GroupsScreen,
    NewEntryScreen,
    ProfileScreen,
    SignUpScreen,
    TransactionsScreen,
    TransactionInfoScreen,
    MoreScreen;

    companion object {

        fun fromRoute(route: String): AvailableScreens =
            when (route.substringBefore("/")) {
                LoginScreen.name -> LoginScreen;
                BalancesScreen.name -> BalancesScreen;
                EditTransactionScreen.name -> EditTransactionScreen;
                GroupsScreen.name -> GroupsScreen;
                NewEntryScreen.name -> NewEntryScreen;
                ProfileScreen.name -> ProfileScreen;
                SignUpScreen.name -> SignUpScreen;
                TransactionsScreen.name -> TransactionsScreen;
                TransactionInfoScreen.name -> TransactionInfoScreen;
                MoreScreen.name -> MoreScreen;
                else -> throw Exception("Route companion object navigation error")
            }

    }
}