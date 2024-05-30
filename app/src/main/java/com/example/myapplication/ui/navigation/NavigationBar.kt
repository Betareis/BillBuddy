package com.example.myapplication.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import com.example.myapplication.ui.screens.LoginScreen
import com.example.myapplication.ui.screens.BalancesScreen
import com.example.myapplication.ui.screens.EditTransactionScreen
import com.example.myapplication.ui.screens.groups.GroupsScreen
import com.example.myapplication.ui.screens.NewEntryScreen
import com.example.myapplication.ui.screens.ProfileScreen
import com.example.myapplication.ui.screens.SignUpScreen
import com.example.myapplication.ui.screens.TransactionsScreen
import com.example.myapplication.ui.screens.TransactionInfoScreen

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null,
    val route: String
)

val groupsTab = TabBarItem(title = "Groups", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home, 0, AvailableScreens.GroupsScreen.name)
val profileTab = TabBarItem(title = "Profile", selectedIcon = Icons.Filled.Notifications, unselectedIcon = Icons.Outlined.Notifications, badgeAmount = 7, AvailableScreens.ProfileScreen.name)
val moreTab = TabBarItem(title = "More", selectedIcon = Icons.Filled.List, unselectedIcon = Icons.Outlined.List, 0, AvailableScreens.LoginScreen.name)

val tabBarItems = listOf(groupsTab, profileTab, moreTab)
val AvailableScreensList = listOf(AvailableScreens.GroupsScreen.name, AvailableScreens.ProfileScreen.name, moreTab)

@Composable
fun NavigationBarScreen() {
    val navController: NavHostController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(bottomBar = { TabView(tabBarItems, navController) }) { innerpadding ->
            NavHost(navController = navController, startDestination = AvailableScreens.GroupsScreen.name, modifier = Modifier.padding(innerpadding)) {
                composable( AvailableScreens.GroupsScreen.name){
                    GroupsScreen(navController)
                }
                composable( AvailableScreens.ProfileScreen.name){
                    ProfileScreen(navController)
                }
                composable(moreTab.title) {
                    MoreView()
                }
            }
        }
    }
}

@Composable
fun TabView(tabBarItems: List<TabBarItem>, navController: NavController) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    NavigationBar {
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                selected = selectedTabIndex == index,
                onClick = {
                        selectedTabIndex = index
                        navController.navigate(tabBarItem.route) {
                            /*popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }*/
                            launchSingleTop = true
                            restoreState = true
                    }
                },
                icon = {
                    TabBarIconView(
                        isSelected = selectedTabIndex == index,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title,
                        badgeAmount = tabBarItem.badgeAmount
                    )
                },
                label = { Text(tabBarItem.title) }
            )
        }
    }
}

@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    badgeAmount: Int? = null
) {
    BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
        Icon(
            imageVector = if (isSelected) selectedIcon else unselectedIcon,
            contentDescription = title
        )
    }
}

@Composable
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(count.toString())
        }
    }
}

@Composable
fun MoreView() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Settings")
        Text("Logout")
    }
}

fun NavigationBar(){


}