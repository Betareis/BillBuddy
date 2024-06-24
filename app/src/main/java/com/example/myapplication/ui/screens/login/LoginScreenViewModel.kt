package com.example.myapplication.ui.screens.login


import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.screens.FetchingState
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore;
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginScreenViewModel :ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    val state: MutableState<FetchingState> = mutableStateOf(FetchingState.IDLE)

    fun logUserIn(email: String, password: String, onSuccess: () -> Unit, onError: () -> Unit) = viewModelScope.launch {
        state.value = FetchingState.LOADING.withMessage("Wait...")
        delay(500)
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    state.value = FetchingState.SUCCESS.withMessage("Success!")

                    Log.d("LOGIN", "Success logging in TASK: ${task.result}")

                    onSuccess()


                } else {
                    Log.d("LOGIN", "Can't log in TASK: ${task.exception?.stackTrace.toString()}")
                    val message = getMessageForFirebaseExceptionLogin(task.exception)
                    state.value = FetchingState.FAILED_INSTANCE.withMessage(message)
                    onError()
                }
            }
        } catch (e: Exception) {
            val message = getMessageForFirebaseExceptionLogin(e)
            state.value = FetchingState.FAILED.withMessage(message)
            Log.d("LOGIN", "loginUser exception")
        }
    }
}


fun getMessageForFirebaseExceptionLogin(exception: Exception?): String {
    var exceptionMessage = ""
    if (exception != null) {
        exceptionMessage = exception.localizedMessage.orEmpty()
        Log.d("EXC", exception.message.toString())
    }
    return when {
        exceptionMessage.contains("credentials", true) -> {
            "Invalid credentials"
        }
        exceptionMessage.contains("code", true) -> {
            "Code expired"
        }
        exceptionMessage.contains("email", true) -> {
            "Invalid email"
        }
        exceptionMessage.contains("exists", true) -> {
            "User with this email already exists"
        }
        exceptionMessage.contains("operation", true) -> {
            "Web operation failed"
        }
        exceptionMessage.contains("invalid user", true) -> {
            "Invalid user"
        }
        exceptionMessage.contains("multi-factor authentication required", true) -> {
            "Multi-factor authentication required"
        }
        exceptionMessage.contains("recent login required", true) -> {
            "Recent login required"
        }
        exceptionMessage.contains("firebase auth login failed", true) -> {
            "Firebase auth login failed"
        }
        else -> {
            "Firebase login failed, try again later"
        }
    }
}
