package com.example.myapplication.data.model

import java.util.Date

data class DCTransaction(
    val name: String = "",
    val amount: Double,
    val payedBy: MutableList<User>,
    val containedUsers: MutableList<User>,
)
