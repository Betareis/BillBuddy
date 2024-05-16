package com.example.myapplication.ui.navigation

import android.view.MenuItem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.Menu
import com.example.myapplication.R


fun NavigationBar(onItemSelected: (String) -> Unit){

    val menuItems = listOf(
        MenuItemData("Groups", R.drawable.baseline_groups_24, "groups"),
        MenuItemData("Profile", R.drawable.baseline_account_circle_24, "profile"),
        MenuItemData("More", R.drawable.baseline_more_24, "more")
    )
}

data class MenuItemData(val title: String, val icon: Int, val id: String) {

}