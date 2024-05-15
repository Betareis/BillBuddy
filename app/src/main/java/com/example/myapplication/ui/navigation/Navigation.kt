package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screens.LoginScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AvailableScreens.LoginScreen.name
    )
    {
        composable( AvailableScreens.LoginScreen.name){
            LoginScreen(navController = navController)
        }/*
        composable( AvaliableScreens.MainScreen.name){
            MainScreen(navController)
        }
        composable( AvaliableScreens.MyCocktailsScreen.name){
            MyCocktailsScreen(navController)
        }
        composable( AvaliableScreens.LoginScreen.name){
            LoginScreen(navController)
        }
        composable( AvaliableScreens.RegisterScreen.name){
            RegisterScreen(navController)
        }
        composable( AvaliableScreens.CocktailSearchScreen.name){
            CocktailSearchScreen(navController)
        }
        composable( AvaliableScreens.CocktailAddScreen.name){
            CocktailAddScreen(navController)
        }
        composable(
            "${AvaliableScreens.CocktailDetailsScreen.name}/?cocktail={cocktail}",
            arguments = listOf(
                navArgument("cocktail") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val encodedCocktail = backStackEntry.arguments?.getString("cocktail")
            val decodedCocktail = Uri.decode(encodedCocktail)
            CocktailDetailsScreen(navController, decodedCocktail)
        }*/

    }
}