package com.example.myapplication.data.model

data class Transaction(
    val id: String? = "",
    val name: String = "",
    val amount: Double = 0.0,
    //val payedBy: MutableList<User>,
    //val containedUsers: MutableList<User>,
)
