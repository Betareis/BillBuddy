package com.example.myapplication.data.model

data class User(
    val id: String?,
    val displayName: String,
    //val balance: Double = 0.0,
) {
    fun toMap(): MutableMap<String, Any?> {
        return mutableMapOf(
            "user_id" to this.id,
            "display_name" to this.displayName
        )
    }
}
