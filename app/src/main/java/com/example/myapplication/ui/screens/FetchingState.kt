package com.example.myapplication.ui.screens
enum class FetchingState(var message: String) {
    IDLE(""),
    FAILED("Error message is required"),
    LOADING("Loading message is required"),
    SUCCESS("Success message is required");

    fun withMessage(newMessage: String): FetchingState {
        message = newMessage
        return this
    }

    fun get(): String {
        return message
    }

    companion object {
        val FAILED_INSTANCE = FAILED
    }
}
