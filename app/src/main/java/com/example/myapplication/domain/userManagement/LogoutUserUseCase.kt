package com.example.myapplication.domain.userManagement

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor() {
    private val auth: FirebaseAuth = Firebase.auth
    operator fun invoke(
        onSuccess: () -> Unit
    ) {
        try {
            auth.signOut()
            onSuccess()
        } catch (e: Exception) {
            Log.d("Critical", "Logout failed")
        }
    }
}