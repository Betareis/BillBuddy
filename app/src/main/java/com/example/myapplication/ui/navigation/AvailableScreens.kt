package com.example.myapplication.ui.navigation


enum class AvailableScreens {
    //EntryLoadingScreen;
    LoginScreen;


    companion object {

        fun fromRoute(route: String): AvailableScreens =
            when (route.substringBefore("/")) {
                LoginScreen.name -> LoginScreen
                else -> throw Exception("Route companion object navigation error")
            }

    }
}