package com.example.myapplication.data.model

data class User(
    val id: String?,
    //val displayName: String,
    val firstname: String,
    val name: String
    //val balance: Double = 0.0,
) {
    fun toMap(): MutableMap<String, Any?> {
        return mutableMapOf(
            "user_id" to this.id,
            "firstname" to this.firstname,
            "name" to this.name
            //"display_name" to this.displayName
        )
    }

    fun getDisplayName(): String {
        return "$firstname $name"
    }

    fun getPayPalName(): String? {
        return null
    }
}
