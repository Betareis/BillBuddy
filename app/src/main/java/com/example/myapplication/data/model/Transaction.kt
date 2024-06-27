package com.example.myapplication.data.model

data class Transaction(
    val id: String? = "",
    val name: String = "",
    val payedBy: String = "",
    val date: String = "",
    val amount: Double = 0.0,
)
