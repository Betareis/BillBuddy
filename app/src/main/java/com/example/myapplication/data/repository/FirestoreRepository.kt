package com.example.myapplication.data.repository

import android.util.Log

import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.myapplication.data.model.Group
import com.example.myapplication.data.model.SingleAmount
import com.example.myapplication.data.model.Transaction
import com.example.myapplication.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.toObject

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
            val payedBy = document.getString("payedBy") ?: ""
            // Access transactions subcollection (assuming it exists)
            Transaction(name = name, id = id, amount = amount, payedBy = payedBy)
        }.toMutableList()

        return DataRequestWrapper(transactions, "", null) // Assuming DataRequestWrapper structure
    }

    suspend fun getSingleTransaction(
        transactionId: String, groupId: String
    ): DataRequestWrapper<Transaction, String, Exception> {
        if (auth.currentUser == null) return DataRequestWrapper(exception = Exception("not authenticated"));

        return try {
            val transaction =
                firestore.collection("groups").document(groupId).collection("transactions")
                    .document(transactionId).get().await()
            val transactionObject = transaction.toObject<Transaction>()
            val transactionObjectData = Transaction(
                transaction.id,
                transactionObject!!.name,
                transactionObject.payedBy,
                transactionObject.date,
                transactionObject.amount,
            )
            DataRequestWrapper(data = transactionObjectData)
        } catch (e: Exception) {
            DataRequestWrapper(exception = Exception("Cant get Transaction $transactionId"))
        }
    }

    suspend fun getGroups(userId: String): DataRequestWrapper<MutableList<Group>, String, Exception> {

        if (auth.currentUser == null) return DataRequestWrapper(exception = Exception("not authenticated"));


        //!Todo: testing if the user is not defined
        val user = firestore.collection("users").document(userId).get().await()

        val groupListUser = user.get("groups") as List<*>

        val groups = mutableListOf<Group>()

        for (groupId in groupListUser) {
            val documentSnapshot =
                firestore.collection("groups").document(groupId.toString()).get().await()
            if (documentSnapshot.exists()) {
                val group = documentSnapshot.toObject<Group>()
                if (group != null) {
                    val name = documentSnapshot.get("name") as String
                    groups.add(Group(documentSnapshot.id, name, emptyList()))
                } else {
                    return DataRequestWrapper(exception = Exception("document structure mismatch in a group document"))
                    // Handle the case where parsing failed (e.g., document structure mismatch)
                }
            } else {
                return DataRequestWrapper(exception = Exception("Failed to get a group document"))
            }
        }
        return DataRequestWrapper(data = groups)
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

    suspend fun getSingleAmounts(
        groupId: String, transactionId: String
    ): DataRequestWrapper<MutableList<SingleAmount>, String, Exception> {

        return try {
            val singleAmountsSnapshot =
                firestore.collection("groups").document(groupId).collection("transactions")
                    .document(transactionId).collection("singleAmount").get()
                    .await()

            if (singleAmountsSnapshot.isEmpty) {
                return DataRequestWrapper(exception = Exception("Error get singleAmounts"))
            }

            val dataList = singleAmountsSnapshot.documents.map { document ->
                SingleAmount(id = document.id, amount = document.getDouble("amount") ?: 0.0)
            }.toMutableList()

            DataRequestWrapper(data = dataList)

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
    ): DataRequestWrapper<Transaction, String, Exception> {
        return try {
            //val auth = FirebaseAuth.getInstance()
            //val currentUser = auth.currentUser

            //if (currentUser != null) {
            //val userId = currentUser.uid

            val groupDocumentRef = firestore.collection("groups").document(groupId)

            val transactionCollectionRef = groupDocumentRef.collection("transactions")
            val newTransactionDocumentRef = transactionCollectionRef.document()

            //Todo: should be checked
            newTransactionDocumentRef.set(transactionData).await()

            val getTransaction = newTransactionDocumentRef.get().await()

            val getTransactionObject = getTransaction.toObject<Transaction>()

            val transactionObject = Transaction(
                getTransaction!!.id,
                getTransactionObject!!.name,
                getTransactionObject.payedBy,
                getTransactionObject.date,
                getTransactionObject.amount
            )

            DataRequestWrapper(data = transactionObject)
        } catch (e: Exception) {
            Log.d("ADD_TRANSACTION_GROUP_RESPONSE", e.stackTraceToString())
            DataRequestWrapper(exception = e)
        }
    }

    private fun hasNonDoubleValues(map: Map<String, Any>): Boolean {
        return map.any { (_, value) -> value !is Double }
    }

    suspend fun setSingleAmounts(
        groupId: String, transactionId: String, singleAmountData: Map<String, Any>
    ) {

        //!Todo: Error message (handling)
        if (hasNonDoubleValues(singleAmountData)) return;
        try {
            val transactionDocumentRef =
                firestore.collection("groups").document(groupId).collection("transactions")
                    .document(transactionId)
            val singleAmountCollectionRef = transactionDocumentRef.collection("singleAmount")

            val batch = firestore.batch() // Create a batch write operation

            singleAmountData.forEach { (key, value) ->
                val docRef = singleAmountCollectionRef.document(key) // Reference to document
                val data = mapOf<String, Any>(
                    "amount" to value as Double   // Extract "amount" field as document value
                )

                batch.set(docRef, data) // Add data to the batch for each document
            }
            batch.commit()
        } catch (e: Exception) {
            Log.d("Error Update Collection SingleAmount", e.stackTraceToString())
        }
    }


    suspend fun setBalanceForUserInGroup(
        userId: String, groupId: String, amount: Double
    ) {
        try {
            val groupDocumentRef =
                firestore.collection("groups").document(groupId).collection("balances")
                    .document(userId)

            if (groupDocumentRef.get().await().exists()) {
                val oldAmount = groupDocumentRef.get().await().getDouble("balance")

                groupDocumentRef.update("balance", (amount + oldAmount!!)).await()
            } else {
                groupDocumentRef.set(mapOf("balance" to amount)).await()
            }

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
