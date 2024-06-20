package com.example.myapplication.domain.userManagement

import android.util.Log
import com.example.myapplication.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor() {
    private val auth: FirebaseAuth = Firebase.auth
    operator fun invoke(
        userData: Map<String, Any>, onSuccess: () -> Unit
    ) {
        if (userData.isEmpty() || userData["email"].toString().isBlank()
            || userData["password"].toString().isBlank()
            || userData["firstname"].toString().isBlank()
            || userData["name"].toString().isBlank()
        ) {
            return
        }
        //!Log.d("test", userData.toString())
        try {
            auth.createUserWithEmailAndPassword(
                userData["email"].toString(), userData["password"].toString()

            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //val displayName = task.result.user?.email?.split('@')?.get(0)

                    val firstname = userData["firstname"].toString()
                    val name = userData["name"].toString()

                    createUserInDifferentCollection(firstname, name, auth)
                    onSuccess()
                } else {
                    Log.d("Register", task.exception.toString())

                }
            }
        } catch (e: Exception) {
            Log.e("Register", "Exception during user registration", e)
        }
    }
}

fun createUserInDifferentCollection(firstname: String, name: String, auth: FirebaseAuth) {
    val userId = auth.currentUser?.uid
    val user = User(id = userId, firstname, name)

    if (userId != null) {
        val usersCollection = FirebaseFirestore.getInstance().collection("users")

        // We add the user with same document id as user id
        usersCollection.document(userId).set(user.toMap())
    }
}