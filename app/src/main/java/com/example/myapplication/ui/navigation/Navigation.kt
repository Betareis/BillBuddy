package com.example.myapplication.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.screens.*
import com.example.myapplication.ui.screens.edittransaction.EditTransactionScreen
import com.example.myapplication.ui.screens.transactions.TransactionsScreen
import com.example.myapplication.ui.screens.groups.GroupsScreen
import com.example.myapplication.ui.screens.login.LoginScreen
import com.example.myapplication.ui.screens.newentry.NewEntryScreen
import com.example.myapplication.ui.screens.profile.ProfileScreen
import com.example.myapplication.ui.screens.signup.SignUpScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(innerPaddingValues: PaddingValues) {
    val navController = rememberNavController()
    val isUserLoggedIn = FirebaseAuth.getInstance().currentUser != null

    NavHost(
        navController = navController,
        startDestination = if (isUserLoggedIn) AvailableScreens.GroupsScreen.name else AvailableScreens.LoginScreen.name,
        modifier = Modifier.padding(innerPaddingValues)
    ) {
        loginNav(navController)
        mainNav(navController)
        transactionNav(navController)
    }
}

private fun NavGraphBuilder.loginNav(navController: NavHostController) {
    composable(AvailableScreens.LoginScreen.name) { LoginScreen(navController) }
    composable(AvailableScreens.SignUpScreen.name) { SignUpScreen(navController) }
}

private fun NavGraphBuilder.mainNav(navController: NavHostController) {
    composable(AvailableScreens.BalancesScreen.name) { BalancesScreen(navController) }
    composable(AvailableScreens.GroupsScreen.name) { GroupsScreen(navController) }
    composable(AvailableScreens.ProfileScreen.name) { ProfileScreen(navController) }
    composable(AvailableScreens.MoreScreen.name) { MoreScreen(navController) }
}

private fun NavGraphBuilder.transactionNav(navController: NavHostController) {
    composable(
        "${AvailableScreens.EditTransactionScreen.name}/?groupId={groupId}&transactionId={transactionId}&transactionName={transactionName}",
        arguments = listOf(
            navArgument("groupId") { type = NavType.StringType },
            navArgument("transactionId") { type = NavType.StringType },
            navArgument("transactionName") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val groupId = backStackEntry.arguments?.getString("groupId")
        val transactionId = backStackEntry.arguments?.getString("transactionId")
        val transactionName = backStackEntry.arguments?.getString("transactionName")
        if (groupId != null && transactionId != null && transactionName != null) {
            EditTransactionScreen(navController, groupId, transactionId, transactionName)
        } else {
            LoginScreen(navController)
        }
    }

    composable(
        "${AvailableScreens.NewEntryScreen.name}/?groupId={groupId}",
        arguments = listOf(navArgument("groupId") { type = NavType.StringType })
    ) { backStackEntry ->
        val groupId = backStackEntry.arguments?.getString("groupId")
        Log.d("NavigationArgs", "groupId $groupId")
        if (groupId != null) {
            NewEntryScreen(navController, groupId)
        }
    }

    composable(
        "${AvailableScreens.TransactionsScreen.name}/?groupId={groupId}&groupName={groupName}",
        arguments = listOf(
            navArgument("groupId") { type = NavType.StringType },
            navArgument("groupName") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val groupId = backStackEntry.arguments?.getString("groupId")
        val groupName = backStackEntry.arguments?.getString("groupName")
        if (groupId != null && groupName != null) {
            TransactionsScreen(navController, groupId, groupName)
        } else {
            LoginScreen(navController)
        }
    }

    composable(
        "${AvailableScreens.TransactionInfoScreen.name}/?groupId={groupId}&transactionId={transactionId}&transactionName={transactionName}",
        arguments = listOf(
            navArgument("groupId") { type = NavType.StringType },
            navArgument("transactionId") { type = NavType.StringType },
            navArgument("transactionName") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val groupId = backStackEntry.arguments?.getString("groupId")
        val transactionId = backStackEntry.arguments?.getString("transactionId")
        val transactionName = backStackEntry.arguments?.getString("transactionName")
        if (groupId != null && transactionId != null && transactionName != null) {
            TransactionInfoScreen(navController, groupId, transactionId, transactionName)
        } else {
            LoginScreen(navController)
        }
    }
}


/*import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.screens.login.LoginScreen
import com.example.myapplication.ui.screens.BalancesScreen
import com.example.myapplication.ui.screens.MoreScreen
import com.example.myapplication.ui.screens.edittransaction.EditTransactionScreen
import com.example.myapplication.ui.screens.groups.GroupsScreen
import com.example.myapplication.ui.screens.newentry.NewEntryScreen
import com.example.myapplication.ui.screens.profile.ProfileScreen
import com.example.myapplication.ui.screens.signup.SignUpScreen
import com.example.myapplication.ui.screens.transactions.TransactionsScreen
import com.example.myapplication.ui.screens.TransactionInfoScreen
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.myapplication.ui.screens.*


@Composable
fun AppNavigation(innerPaddingValues: PaddingValues) {
    val navController: NavHostController = rememberNavController()

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val isUserLoggedIn = auth.currentUser != null;

    NavHost(
        navController = navController,
        startDestination = if (isUserLoggedIn) "group_screen" else "login_screen",
        //startDestination = AvailableScreens.MoreScreen.name,
        modifier = Modifier.padding(innerPaddingValues)
    ) {
        composable(AvailableScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }
        composable(AvailableScreens.BalancesScreen.name) {
            BalancesScreen(navController)
        }

        composable(
            "${AvailableScreens.EditTransactionScreen.name}/?groupId={groupId}&transactionId={transactionId}&transactionName={transactionName}",
            arguments = listOf(
                navArgument("groupId") {
                    type = NavType.StringType
                },
                navArgument("transactionId") {
                    type = NavType.StringType
                },
                navArgument("transactionName") {
                    type = NavType.StringType
                },
            )
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")
            val transactionId = backStackEntry.arguments?.getString("transactionId")
            val transactionName = backStackEntry.arguments?.getString("transactionName")

            if (groupId != null && transactionId != null && transactionName != null) {
                EditTransactionScreen(navController, groupId, transactionId, transactionName)
            } else LoginScreen(navController = navController)
        }


        /*composable(AvailableScreens.EditTransactionScreen.name) {
            EditTransactionScreen(navController)
        }*/
        composable(AvailableScreens.GroupsScreen.name) {
            GroupsScreen(navController)
        }

        composable(
            "${AvailableScreens.NewEntryScreen.name}/?groupId={groupId}", arguments = listOf(
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
        composable(AvailableScreens.MoreScreen.name) {
            MoreScreen(navController)
        }

        //Todo: Should be remove in production
        /*composable(AvailableScreens.TransactionsScreen.name) {
            TransactionsScreen(navController, "bla")
        }*/

        composable(
            "${AvailableScreens.TransactionsScreen.name}/?groupId={groupId}&groupName={groupName}",
            arguments = listOf(navArgument("groupId") {
                type = NavType.StringType
            }, navArgument("groupName") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")
            val groupName = backStackEntry.arguments?.getString("groupName")

            //Log.d("NavigationArgs", "groupId: $groupId, groupName: $groupName")

            if (groupId != null && groupName != null) {
                TransactionsScreen(navController, groupId, groupName)
            } else LoginScreen(navController = navController)
        }

        composable(
            "${AvailableScreens.TransactionInfoScreen.name}/?groupId={groupId}&transactionId={transactionId}&transactionName={transactionName}",
            arguments = listOf(
                navArgument("groupId") {
                    type = NavType.StringType
                },
                navArgument("transactionId") {
                    type = NavType.StringType
                },
                navArgument("transactionName") {
                    type = NavType.StringType
                },
            )
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")
            val transactionId = backStackEntry.arguments?.getString("transactionId")
            val transactionName = backStackEntry.arguments?.getString("transactionName")

            if (groupId != null && transactionId != null && transactionName != null) {
                TransactionInfoScreen(navController, groupId, transactionId, transactionName)
            } else LoginScreen(navController = navController)
        }
    }
}*/

