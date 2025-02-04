package com.example.myapplication.ui.navigation

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.myapplication.ui.screens.JoinGroupScreen
import com.example.myapplication.ui.screens.*
import com.example.myapplication.ui.screens.edittransaction.EditTransactionScreen
import com.example.myapplication.ui.screens.groups.GroupsScreen
import com.example.myapplication.ui.screens.login.LoginScreen
import com.example.myapplication.ui.screens.newentry.NewEntryScreen
import com.example.myapplication.ui.screens.profile.ProfileScreen
import com.example.myapplication.ui.screens.signup.SignUpScreen
import com.example.myapplication.ui.screens.transactioninfo.TransactionInfoScreen
import com.example.myapplication.ui.screens.transactions.TransactionsBalancesLayout
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
        deepLinksNav(navController)
    }
}

private fun NavGraphBuilder.loginNav(navController: NavHostController) {
    composable(route = AvailableScreens.LoginScreen.name, enterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)
        )
    }, exitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)
        )
    }) { LoginScreen(navController) }
    composable(route = AvailableScreens.SignUpScreen.name, enterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)
        )
    }, exitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)
        )
    }) { SignUpScreen(navController) }
}

private fun NavGraphBuilder.mainNav(navController: NavHostController) {
    composable(route = AvailableScreens.GroupsScreen.name, enterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)
        )
    }, exitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)
        )
    }) { GroupsScreen(navController) }
    composable(route = AvailableScreens.ProfileScreen.name, enterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)
        )
    }, exitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)
        )
    }) { ProfileScreen(navController) }
    composable(route = AvailableScreens.MoreScreen.name, enterTransition = {
        fadeIn(tween(500))
    }, exitTransition = {
        fadeOut(tween(500))
    }) { MoreScreen(navController) }
}

private fun NavGraphBuilder.transactionNav(navController: NavHostController) {
    composable(
        "${AvailableScreens.EditTransactionScreen.name}/?groupId={groupId}&transactionId={transactionId}&transactionName={transactionName}&transactionAmount={transactionAmount}&transactionDate={transactionDate}&payedBy={payedBy}",
        arguments = listOf(navArgument("groupId") { type = NavType.StringType },
            navArgument("transactionId") { type = NavType.StringType },
            navArgument("transactionName") { type = NavType.StringType },
            navArgument("transactionAmount") { type = NavType.FloatType },
            navArgument("transactionDate") { type = NavType.StringType },
            navArgument("payedBy") { type = NavType.StringType })
    ) { backStackEntry ->
        val groupId = backStackEntry.arguments?.getString("groupId")
        val transactionId = backStackEntry.arguments?.getString("transactionId")
        val transactionName = backStackEntry.arguments?.getString("transactionName")
        val transactionAmount =
            backStackEntry.arguments?.getFloat("transactionAmount")?.toDouble() ?: 0.0
        val transactionDate = backStackEntry.arguments?.getString("transactionDate") ?: ""
        val payedBy = backStackEntry.arguments?.getString("payedBy") ?: ""
        if (groupId != null && transactionId != null && transactionName != null) {
            EditTransactionScreen(
                navController,
                groupId,
                transactionId,
                transactionName,
                transactionAmount,
                transactionDate,
                payedBy
            )
        } else {
            LoginScreen(navController)
        }
    }

    composable(
        route = "${AvailableScreens.NewEntryScreen.name}/?groupId={groupId}",
        enterTransition = {
            fadeIn(tween(500))
        },
        exitTransition = {
            fadeOut(tween(500))
        },
        arguments = listOf(navArgument("groupId") { type = NavType.StringType })
    ) { backStackEntry ->
        val groupId = backStackEntry.arguments?.getString("groupId")
        Log.d("NavigationArgs", "groupId $groupId")
        if (groupId != null) {
            NewEntryScreen(navController, groupId)
        }
    }

    composable(
        "${AvailableScreens.TransactionsBalancesLayout.name}/?groupId={groupId}&groupName={groupName}",
        arguments = listOf(navArgument("groupId") { type = NavType.StringType },
            navArgument("groupName") { type = NavType.StringType })
    ) { backStackEntry ->
        val groupId = backStackEntry.arguments?.getString("groupId")
        val groupName = backStackEntry.arguments?.getString("groupName")
        if (groupId != null && groupName != null) {
            TransactionsBalancesLayout(navController, groupId, groupName)
        } else {
            LoginScreen(navController)
        }
    }

    composable(
        "${AvailableScreens.TransactionInfoScreen.name}/?groupId={groupId}&transactionId={transactionId}&transactionName={transactionName}&" +
                "transactionAmount={transactionAmount}&transactionDate={transactionDate}&payedBy={payedBy}",
        arguments = listOf(navArgument("groupId") { type = NavType.StringType },
            navArgument("transactionId") { type = NavType.StringType },
            navArgument("transactionName") { type = NavType.StringType },
            navArgument("transactionAmount") { type = NavType.FloatType },
            navArgument("transactionDate") { type = NavType.StringType },
            navArgument("payedBy") { type = NavType.StringType })
    ) { backStackEntry ->
        val groupId = backStackEntry.arguments?.getString("groupId")
        val transactionId = backStackEntry.arguments?.getString("transactionId")
        val transactionName = backStackEntry.arguments?.getString("transactionName")
        val transactionAmount =
            backStackEntry.arguments?.getFloat("transactionAmount")?.toDouble() ?: 0.0
        val transactionDate = backStackEntry.arguments?.getString("transactionDate") ?: ""
        val payedBy = backStackEntry.arguments?.getString("payedBy") ?: ""

        if (groupId != null && transactionId != null && transactionName != null) {
            TransactionInfoScreen(
                navController,
                groupId,
                transactionId,
                transactionName,
                transactionAmount,
                transactionDate,
                payedBy
            )
        } else {
            LoginScreen(navController)
        }
    }
}

private fun NavGraphBuilder.deepLinksNav(navController: NavHostController) {
    composable(
        "addToGroup",
        deepLinks = listOf(navDeepLink {
            uriPattern = "https://www.billbuddy.com/joingroup/?groupId={groupId}"
            action = Intent.ACTION_VIEW
        }),
        arguments = listOf(
            navArgument("groupId") {
                type = NavType.StringType
            },
        )
    ) { backStackEntry ->
        val groupId = backStackEntry.arguments?.getString("groupId")

        val auth = FirebaseAuth.getInstance().currentUser
        val context = LocalContext.current
        if (groupId != null && auth != null) {
            JoinGroupScreen(
                navController, groupId, auth.uid
            )
        } else {
            LoginScreen(navController)
            Toast.makeText(context, "Login first. Then try again.", Toast.LENGTH_SHORT).show()
        }
    }
}
