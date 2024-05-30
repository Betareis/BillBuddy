package com.example.myapplication.data.model

import androidx.compose.ui.graphics.vector.ImageVector


data class DCTransaction(
    val name: String = "",
    val amount: Double,
    //val payedBy: MutableList<User>,
    //val containedUsers: MutableList<User>,
    val icon: ImageVector
)
