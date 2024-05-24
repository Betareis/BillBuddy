package com.example.myapplication.data.model

import java.util.Date

data class Group(
    val name: String = "",
    val transactions: Transactions = Transactions()
)
