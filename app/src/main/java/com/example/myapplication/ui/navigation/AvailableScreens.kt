package com.example.myapplication.ui.navigation


enum class AvailableScreens {
    LoginScreen,
    EditTransactionScreen,
    GroupsScreen,
    NewEntryScreen,
    ProfileScreen,
    SignUpScreen,
    TransactionsBalancesLayout,
    TransactionInfoScreen,
    MoreScreen;

    companion object {

        fun fromRoute(route: String): AvailableScreens =
            when (route.substringBefore("/")) {
                LoginScreen.name -> LoginScreen;
                EditTransactionScreen.name -> EditTransactionScreen;
                GroupsScreen.name -> GroupsScreen;
                NewEntryScreen.name -> NewEntryScreen;
                ProfileScreen.name -> ProfileScreen;
                SignUpScreen.name -> SignUpScreen;
                TransactionsBalancesLayout.name -> TransactionsBalancesLayout;
                TransactionInfoScreen.name -> TransactionInfoScreen;
                MoreScreen.name -> MoreScreen;
                else -> throw Exception("Route companion object navigation error")
            }

    }
}