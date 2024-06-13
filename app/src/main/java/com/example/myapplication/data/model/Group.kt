package com.example.myapplication.data.model

data class Group(
    val id: String? = "",
    val name: String = "",
    val transactions: List<Transaction> = emptyList()  //Transactions()
)
