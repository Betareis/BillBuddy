package com.example.myapplication.data.repository

import android.util.Log

import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.myapplication.data.model.Group
import com.example.myapplication.data.model.Groups
import com.example.myapplication.data.model.Transactions

class FirestoreRepository @Inject constructor() {

    private val firestore = FirebaseFirestore.getInstance()
    //private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    //Group Operations
    suspend fun getGroupsFirestore(): DataRequestWrapper<Groups, String, Exception> {
        val response =
            try {
                    val result = firestore.collection("groups")
                        .get()
                        .await()

                val groups = result.documents.map { document ->S
                    val name = document.getString("name") ?: ""
                    val transactions = document.get("transactions", Transactions::class.java) ?: Transactions()
                    Group(name, transactions)
                }.toMutableList()


                Groups(groups)
            } catch (e: Exception) {
                Log.d("RESPONSE", e.stackTraceToString())
                return DataRequestWrapper(exception = e)
            }

        return DataRequestWrapper(data = response)
    }
}
