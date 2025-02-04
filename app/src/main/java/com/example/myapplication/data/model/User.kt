package com.example.myapplication.data.model

data class User(
    val id: String?,
    val firstname: String,
    val name: String,
    val payPalName: String
) {
    fun toMap(): MutableMap<String, Any?> {
        return mutableMapOf(
            "user_id" to this.id,
            "firstname" to this.firstname,
            "name" to this.name,
            "paypalAddress" to this.payPalName
        )
    }

    fun getDisplayName(): String {
        return "$firstname $name"
    }

    fun retrievePayPalName(): String {
        return payPalName
    }
}
