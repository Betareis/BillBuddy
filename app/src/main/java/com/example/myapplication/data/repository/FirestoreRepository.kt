package com.example.myapplication.data.repository

import android.util.Log

import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.myapplication.data.model.Group
import com.example.myapplication.data.model.Transaction

class FirestoreRepository @Inject constructor() {

    private val firestore = FirebaseFirestore.getInstance()
    //private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    suspend fun getTransactionsGroup(groupId: String): DataRequestWrapper<MutableList<Transaction>, String, Exception> {


        val result = firestore.collection("groups").document(groupId).collection("transactions")
            .get()
            .await()

        //Log.d("SIZE", result.size().toString())


        val transactions = result.documents.map { document ->
            val name = document.getString("name") ?: ""
            val id = document.id;
            val amount = document.getDouble("amount") ?: 0.0
            // Access transactions subcollection (assuming it exists)

            Transaction(name, amount)


        }.toMutableList()


        //val groupId = "2gQ9AzCpEI8jCdEg4Ezw" // Replace with your actual groupId
        //val groupRef = FirebaseFirestore.getInstance().collection("groups").document(groupId).collection("transaction")

        ///groups/2gQ9AzCpEI8jCdEg4Ezw/transactions
        /*val result2 = firestore.collection("transactions")
            .get()
            .await()


        Log.d("SIZE", result2.toString())
*/
        /*val transactions_data = result2.documents.map { document ->

            val name = document.getString("name") ?: ""
            val amount = document.get("amount") as Number

            Transaction(name, amount)
        }.toMutableList()

        Log.d("DABA", Transactions(transactions_data).toString())
*/



        return DataRequestWrapper(transactions, "", null) // Assuming DataRequestWrapper structure
    }


    suspend fun getGroups(): DataRequestWrapper<MutableList<Group>, String, Exception> {


        val result = firestore.collection("groups")
            .get()
            .await()

        //Log.d("SIZE", result.size().toString())

        val groups = result.documents.map { document ->
            val name = document.getString("name") ?: ""
            val id = document.id;
            // Access transactions subcollection (assuming it exists)

            val transactionsRef = document.reference.collection("transactions")


            // Fetch transactions using the subcollection reference
            val transactions = try {
                transactionsRef.get().await().toObjects(Transaction::class.java) ?: emptyList()
            } catch (e: Exception) {
                Log.d("TEST_C", "Error retrieving transactions: ${e.message}")
                emptyList() // Return empty list on error
            }

            Log.d("DATTA", "DATA: " + transactions.toMutableList().toString())

            // Create Group object with retrieved transactions
            Group(id, name, transactions)
        }.toMutableList()


        //val groupId = "2gQ9AzCpEI8jCdEg4Ezw" // Replace with your actual groupId
        //val groupRef = FirebaseFirestore.getInstance().collection("groups").document(groupId).collection("transaction")

        ///groups/2gQ9AzCpEI8jCdEg4Ezw/transactions
        /*val result2 = firestore.collection("transactions")
            .get()
            .await()


        Log.d("SIZE", result2.toString())
*/
        /*val transactions_data = result2.documents.map { document ->

            val name = document.getString("name") ?: ""
            val amount = document.get("amount") as Number

            Transaction(name, amount)
        }.toMutableList()

        Log.d("DABA", Transactions(transactions_data).toString())
*/



        return DataRequestWrapper(groups, "", null) // Assuming DataRequestWrapper structure
    }


    /*suspend fun getGroups(): DataRequestWrapper<Groups, String, Exception> {
        val result = firestore.collection("groups")
            .get()
            .await()

        val groups = result.documents.map { document ->
            val name = document.getString("name") ?: ""

            val transactionsRefArray = document.get("transactions")


            val transactions = try {
                 (transction in transactionsRefArray)

                }
            } catch (e: Exception) {
                Log.d("TEST_C", "Fehler!!!")
                // Handle potential error during transaction retrieval (or if no subcollection exists)
                emptyList() // Return empty list if error or no subcollection
            }


            /*val transactions = try {
                val querySnapshot = transactionsRef.get().await() // Wait for transactions retrieval (if exists)
                querySnapshot.documents.map { transactionDoc ->
                    // Access transaction data from transactionDoc
                    val transactionName = transactionDoc.getString("name") ?: ""
                    val transactionAmount = transactionDoc.getDouble("amount") ?: 0.0

                    Log.d("TAG", "Transaction: " + Transaction(transactionName, transactionAmount).toString())
                    Transaction(transactionName, transactionAmount)
                }
            } catch (e: Exception) {
                Log.d("TEST_C", "Fehler!!!")
                // Handle potential error during transaction retrieval (or if no subcollection exists)
                emptyList() // Return empty list if error or no subcollection
            }*/
            //Log.d("TEST", Group(name, transactions).toString() + "size transactions array: " + transactions.size)

            Group(name, transactions)
        }.toMutableList()

        Log.d("TAG", "data: " + groups.toString())

        return DataRequestWrapper(data = Groups(groups))

        //return Groups(groups) // Handle potential errors
    }*/


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
