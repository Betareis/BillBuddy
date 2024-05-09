package com.example.myapplication.data.model

import java.util.Date

data class Transaction(

    val name: String = "",
    val amount: Double,
    val date: Date,
    val payedBy: String
)
