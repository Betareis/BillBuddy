package com.example.myapplication.data.repository

import android.util.Log

import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.myapplication.data.model.Group
import com.example.myapplication.data.model.Groups
import com.example.myapplication.data.model.Transaction
import com.example.myapplication.data.model.Transactions

class FirestoreRepository @Inject constructor() {

    private val firestore = FirebaseFirestore.getInstance()
    //private val auth: FirebaseAuth = FirebaseAuth.getInstance()



    suspend fun getGroupsFirestore(): DataRequestWrapper<Groups, String, Exception> {
        val result = firestore.collection("groups")
            .get()
            .await()

        val groups = result.documents.map { document ->
            val name = document.getString("name") ?: ""

            val transactionsRef = document.reference.collection("transactions")

            val transactions = try {
                val querySnapshot = transactionsRef.get().await() // Wait for transactions retrieval (if exists)
                querySnapshot.documents.map { transactionDoc ->
                    // Access transaction data from transactionDoc
                    val transactionName = transactionDoc.getString("name") ?: ""
                    val transactionAmount = transactionDoc.getDouble("amount") ?: 0.0

                    Log.d("TEST_T", "Transaction: " + Transaction(transactionName, transactionAmount).toString())
                    Transaction(transactionName, transactionAmount)
                }
            } catch (e: Exception) {
                Log.d("TEST_C", "Fehler!!!")
                // Handle potential error during transaction retrieval (or if no subcollection exists)
                emptyList() // Return empty list if error or no subcollection
            }
            Log.d("TEST", Group(name, transactions).toString() + "size transactions array: " + transactions.size)

            Group(name, transactions)
        }.toMutableList()

        return DataRequestWrapper(data = Groups(groups))

        //return Groups(groups) // Handle potential errors
    }


    /*suspend fun getGroupsFirestore(): DataRequestWrapper<Groups, String, Exception> {
        val result = firestore.collection("groups")
            .get()
            .await()

        val groups = result.documents.map { document ->
            val name = document.getString("name") ?: ""
            val transactionsRef = document.reference.collection("transactions")


            val groupWithTransactions = transactionsRef.get()
                .addOnSuccessListener { querySnapshot ->
                    querySnapshot.documents.map { transactionDoc ->
                        // Access transaction data from transactionDoc
                        val transactionName = transactionDoc.getString("name") ?: ""
                        val transactionAmount = transactionDoc.getDouble("amount") ?: 0.0
                        Transaction(transactionName, transactionAmount)
                    }
                }
                .addOnFailureListener { exception ->
                    Group("")
                }


            Log.d("TAG", "DATA: " + groupWithTransactions.result.toString())



            Group(name, emptyList()) // Placeholder for transactions
        }.toMutableList()

        return DataRequestWrapper(data = Groups(groups))

        //Groups(groups) // Handle potential errors
    }*/



    //Group Operations
    /*suspend fun getGroupsFirestore(): DataRequestWrapper<Groups, String, Exception> {
        val response =
            try {
                    val result = firestore.collection("groups")
                        .get()
                        .await()

                val groups = result.documents.map { document ->
                    val name = document.getString("name") ?: ""
                    val transactionsbla = document.get("transactions", Transactions::class.java) ?: Transactions()
                    //Log.d("TAG", "DATA: " + transactions.size.toString())

                    val transactionsRef = document.reference.collection("transactions")

                    var GroupCopy = Group()

                    val test = transactionsRef.get()
                        .addOnSuccessListener { querySnapshot ->
                            val transactions = querySnapshot.documents.map { transactionDoc ->
                                // Access transaction data from transactionDoc (assuming a Transaction class exists)
                                val transactionName = transactionDoc.getString("name") ?: ""
                                val transactionAmount = transactionDoc.getDouble("amount") ?: 0.0
                                Transaction(transactionName, transactionAmount)
                            }
                            Group(name, transactions)
                            // ... proceed with group data
                        }
                        .addOnFailureListener { exception ->
                            // Handle error retrieving transactions
                        }

                    Log.d("TAG", "DATA: " + test.result.)

                    Log.d("TAG", "DATA1: " + name)

                    Group(name, transactionsbla)
                }.toMutableList()


                Groups(groups)
            } catch (e: Exception) {
                Log.d("RESPONSE", e.stackTraceToString())
                return DataRequestWrapper(exception = e)
            }

        return DataRequestWrapper(data = response)
    }*/
}
