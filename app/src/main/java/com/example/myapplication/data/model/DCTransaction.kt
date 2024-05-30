package com.example.myapplication.data.model

data class DCTransaction(
    val name: String,
    val amount: Double,
    val payedBy: MutableList<User>,
    val containedUsers: MutableList<User>
)