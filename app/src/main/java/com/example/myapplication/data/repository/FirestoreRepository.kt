package com.example.myapplication.data.repository

import android.util.Log

import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.myapplication.data.model.Group
import com.example.myapplication.data.model.Transaction
import com.example.myapplication.data.model.User
import com.google.firebase.auth.FirebaseAuth

class FirestoreRepository @Inject constructor() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /*QUERIES*/

    suspend fun getTransactionsGroup(groupId: String): DataRequestWrapper<MutableList<Transaction>, String, Exception> {


        val result =
            firestore.collection("groups").document(groupId).collection("transactions").get()
                .await()

        val transactions = result.documents.map { document ->
            val name = document.getString("name") ?: ""
            val id = document.id;
            val amount = document.getDouble("amount") ?: 0.0
            // Access transactions subcollection (assuming it exists)
            Transaction(name = name, id = id, amount = amount)
        }.toMutableList()

        return DataRequestWrapper(transactions, "", null) // Assuming DataRequestWrapper structure
    }

    suspend fun getGroups(): DataRequestWrapper<MutableList<Group>, String, Exception> {


        val result = firestore.collection("groups").get().await()

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


        return DataRequestWrapper(groups, "", null) // Assuming DataRequestWrapper structure
    }

    suspend fun getUsersOfGroup(groupId: String): DataRequestWrapper<MutableList<User>, String, Exception> {
        val users = mutableListOf<User>()

        try {
            // Gruppe-Dokument abrufen
            val groupDoc = firestore.collection("groups").document(groupId).get().await()
            if (groupDoc.exists()) {
                val members = groupDoc.get("users") as List<*>

                // Benutzerinformationen für jede Benutzer-ID abrufen
                for (userId in members) {
                    val userDoc =
                        firestore.collection("users").document(userId.toString()).get().await()
                    if (userDoc.exists()) {
                        val user = User(
                            id = userDoc.id,
                            firstname = userDoc.getString("firstname") ?: "",
                            name = userDoc.getString("name") ?: "",
                        )
                        users.add(user)
                    }
                }
            }
        } catch (e: Exception) {
            println("Fehler beim Abrufen der Benutzer: ${e.message}")
        }

        return DataRequestWrapper(data = users)
    }

    suspend fun getSingleAmount(
        groupId: String, transactionId: String, userId: String
    ): DataRequestWrapper<Double, String, Exception> {

        return try {
            // Gruppe-Dokument abrufen
            val singleAmountDoc =
                firestore.collection("groups").document(groupId).collection("transactions")
                    .document(transactionId).collection("singleAmount").document(userId).get()
                    .await()
            if (singleAmountDoc.exists()) {
                DataRequestWrapper(data = singleAmountDoc.getDouble("amount"))
            } else {
                DataRequestWrapper(exception = Exception("Document does not exist"))
            }
        } catch (e: Exception) {
            println("Fehler beim Abrufen der singleAmount: ${e.message}")
            DataRequestWrapper(exception = e)
        }
    }


    /*
    suspend fun getUserBalances(groupId: String): MutableList<Balance> {
        val result =
            firestore.collection("groups").document(groupId).collection("balances").get()
                .await()

        val balances = result.documents.map { document ->
            val id = document.id;
            val amount = document.getDouble("balance") ?: 0.0
            Balance( id = id, balance = amount)
        }.toMutableList()

        return DataRequestWrapper(balances, "", null)
    }
    */


    /*suspend fun getUsersOfGroups(groupId: String): DataRequestWrapper<MutableList<User>, String, Exception> {
        val data = firestore.collection("groups").document(groupId).get().await()



        val userList = data.data?.get("users") as List<*>

        val users = ArrayList<User>()

        for (userId in userList) {
            val user =
                getUser(userId.toString(), firestore) // Replace with your user retrieval logic
            if (user != null) {
                users.add(user)
            }
        }

        if (users.isEmpty()) {
            return DataRequestWrapper(exception = Exception("No users in group"))


        }

        return DataRequestWrapper(data = users.toMutableList())
    }

    private suspend fun getUser(userId: String, firestore: FirebaseFirestore): User? {
        return try {
            firestore.collection("users").document(userId).get().await().toObject<User>()
        } catch (e: Error) {
            null;
        }
    }*/


    /********************************************* MUTATIONS CREATE ****************************************************************************/

//TODO: Implemented here or directly in the formComponent
    suspend fun createUser(user: User) {}


    /*Todo: groupId is autogenerated by firestore, maybe remove the groupId in this method or
       create a new data class without groupId*/
    suspend fun createGroup(groupData: Map<String, Any>): DataRequestWrapper<Unit, String, Exception> {
        return try {
            //val auth = FirebaseAuth.getInstance()
            //val currentUser = auth.currentUser
            val db = FirebaseFirestore.getInstance()
            //if (currentUser != null) {
            //val userId = currentUser.uid

            val groupDocumentRef = db.collection("groups")

            val newGroupDocumentRef = groupDocumentRef.document()

            /*val dataWithTransactions = hashMapOf<String, Any>(
                "transactions" to hashMapOf<String, Any>(),
                **groupData** // Spread existing group data
            )*/


            //Todo: should be checked
            newGroupDocumentRef.set(groupData).await()

            DataRequestWrapper(data = Unit)/*} else {
                throw Exception("User ID is null.")
            }*/
        } catch (e: Exception) {
            Log.d("ADD_GROUP_RESPONSE", e.stackTraceToString())
            DataRequestWrapper(exception = e)
        }
    }

    suspend fun createTransactionForGroup(
        groupId: String, transactionData: Map<String, Any>
    ): DataRequestWrapper<Unit, String, Exception> {
        return try {
            //val auth = FirebaseAuth.getInstance()
            //val currentUser = auth.currentUser

            val db = FirebaseFirestore.getInstance()

            //if (currentUser != null) {
            //val userId = currentUser.uid

            val groupDocumentRef = db.collection("groups").document(groupId)

            val transactionCollectionRef = groupDocumentRef.collection("transactions")
            val newTransactionDocumentRef = transactionCollectionRef.document()

            //Todo: should be checked
            newTransactionDocumentRef.set(transactionData).await()

            DataRequestWrapper(data = Unit)/*} else {
                throw Exception("User ID is null.")
            }*/
        } catch (e: Exception) {
            Log.d("ADD_TRANSACTION_GROUP_RESPONSE", e.stackTraceToString())
            DataRequestWrapper(exception = e)
        }
    }

    suspend fun setBalanceForUserInGroup(
        userId: String, groupId: String, amount: Double
    ) {
        try {
            val groupDocumentRef =
                firestore.collection("groups").document(groupId).collection("balances")
                    .document(userId)

            val oldAmount = groupDocumentRef.get().await().getDouble("balance")
            groupDocumentRef.update("balance", oldAmount!! + amount).await()

        } catch (e: Exception) {
            Log.d("Update_Balance_Failed", e.stackTraceToString())
        }
    }


    /********************************************* MUTATIONS UPDATE ****************************************************************************/

//Todo: Update debts for members of specific transaction

    /*private suspend fun updateBalanceOfUserInGroup(groupId: String, transactionData: Transaction): DataRequestWrapper<Unit, String, Exception> {
        return try {
            //val auth = FirebaseAuth.getInstance()
            //val currentUser = auth.currentUser

            val db = FirebaseFirestore.getInstance()

            //if (currentUser != null) {
            //val userId = currentUser.uid

            val groupDocumentRef = db.collection("groups").document(groupId)

            val transactionCollectionRef = groupDocumentRef.collection("transactions")
            val newTransactionDocumentRef = transactionCollectionRef.document()

            newTransactionDocumentRef.set(transactionData).await()

            DataRequestWrapper(data = Unit)
            /*} else {
                throw Exception("User ID is null.")
            }*/
        } catch (e: Exception) {
            Log.d("ADD_TRANSACTION_GROUP_RESPONSE", e.stackTraceToString())
            DataRequestWrapper(exception = e)
        }
    }*/

    /********************************************* MUTATIONS DELETE ****************************************************************************/

    suspend fun deleteTransactionGroup(
        groupId: String, transactionId: String
    ): DataRequestWrapper<Unit, String, Exception> {
        val userId = auth.currentUser?.uid
        return try {
            if (userId != null) {
                firestore.collection("groups").document(groupId).collection("transactions")
                    .document(transactionId).delete().await()

                DataRequestWrapper(data = Unit) // Successfully deleted
            } else {
                throw Exception("groupId or transactionId is null.")
            }
        } catch (e: Exception) {
            Log.d("DELETE_RESPONSE", e.stackTraceToString())
            DataRequestWrapper(exception = e)
        }
    }
}
