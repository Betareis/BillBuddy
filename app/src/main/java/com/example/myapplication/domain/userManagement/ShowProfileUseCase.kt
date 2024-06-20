package com.example.myapplication.domain.userManagement

import android.util.Log
import com.example.myapplication.data.model.User
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShowProfileUseCase @Inject constructor() {
    private val auth: FirebaseAuth = Firebase.auth
    private val firestore: FirebaseFirestore = Firebase.firestore
    suspend operator fun invoke(): DataRequestWrapper<User, String, Exception> {
        val userData = try {
            auth.currentUser?.let {
                firestore.collection("users").document(it.uid).get()
                    .await()
            }
        } catch (e: Exception) {
            Log.d("TEST_C", "Error retrieving user data: ${e.message}")
            null // Return empty list on error
        }

        if (userData != null) {
            return DataRequestWrapper(
                User(
                    id = userData.id,
                    displayName = userData["display_name"].toString()
                ), "", null
            )
        }

        return DataRequestWrapper(exception = Exception("No user data found"))
    }

}
