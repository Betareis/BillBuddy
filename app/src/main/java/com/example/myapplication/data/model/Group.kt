package com.example.myapplication.data.model

data class Group(
    val name: String = "",
    val transactions: List<Transaction> = Transactions()
)
