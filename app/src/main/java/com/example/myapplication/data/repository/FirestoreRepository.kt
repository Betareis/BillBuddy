package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.model.Balance

import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.myapplication.data.model.Group
import com.example.myapplication.data.model.SingleAmount
import com.example.myapplication.data.model.Transaction
import com.example.myapplication.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.toObject

class FirestoreRepository @Inject constructor() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /*QUERIES*/
    suspend fun getUsername(userId: String): DataRequestWrapper<String, String, Exception> {
        return try {
            val result = firestore.collection("users").document(userId).get().await()

            val userObject =
                User(result.id, result.getString("firstname") ?: "", result.getString("name") ?: "", result.getString("paypalAddress") ?: "")

            if (result == null) DataRequestWrapper(data = "")
            else DataRequestWrapper(data = userObject.getDisplayName())

        } catch (e: Exception) {
            DataRequestWrapper(data = "", exception = Exception("Error retrieve the username"))
        }

    }


    suspend fun getTransactionsGroup(groupId: String): DataRequestWrapper<MutableList<Transaction>, String, Exception> {


        val result =
            firestore.collection("groups").document(groupId).collection("transactions").get()
                .await()

        val transactions = result.documents.map { document ->
            val name = document.getString("name") ?: ""
            val id = document.id
            val amount = document.getDouble("amount") ?: 0.0
            val payedBy = document.getString("payedBy") ?: ""
            val date = document.getString("date") ?: ""
            Transaction(name = name, id = id, amount = amount, payedBy = payedBy, date = date)
        }.toMutableList()

        return DataRequestWrapper(transactions, "", null)
    }

    suspend fun getSingleTransaction(
        transactionId: String, groupId: String
    ): DataRequestWrapper<Transaction, String, Exception> {
        if (auth.currentUser == null) return DataRequestWrapper(exception = Exception("not authenticated"))

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

        if (auth.currentUser == null) return DataRequestWrapper(exception = Exception("not authenticated"))



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
                }
            } else {
                return DataRequestWrapper(exception = Exception("Failed to get a group document"))
            }
        }
        return DataRequestWrapper(data = groups)
    }


    suspend fun getBalancesOfGroup(groupId: String): DataRequestWrapper<MutableList<Balance>, String, Exception> {
        try {
            val balancesCollectionSnapshot =
                firestore.collection("groups").document(groupId).collection("balances").get()
                    .await()
            if (balancesCollectionSnapshot.isEmpty) {
                return DataRequestWrapper(exception = Exception("Error get balances collection"))
            }
            val dataList = balancesCollectionSnapshot.documents.map { document ->
                Balance(document.id, document.getDouble("balance")!!)
            }.toMutableList()

            return DataRequestWrapper(data = dataList)

        } catch (e: Exception) {
            return DataRequestWrapper(exception = Exception("Error get group balances"))
        }

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
                            payPalName = userDoc.getString("paypalAddress") ?: ""

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

    suspend fun getSingleAmounts(
        groupId: String, transactionId: String
    ): DataRequestWrapper<MutableList<SingleAmount>, String, Exception> {

        return try {
            val singleAmountsSnapshot =
                firestore.collection("groups").document(groupId).collection("transactions")
                    .document(transactionId).collection("singleAmount").get().await()

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

    /********************************************* MUTATIONS CREATE ****************************************************************************/

    suspend fun createGroup(
        groupData: Map<String, Any>, userId: String
    ): DataRequestWrapper<Unit, String, Exception> {
        return try {
            val db = FirebaseFirestore.getInstance()

            val groupCollectionRef = db.collection("groups")

            val newGroupDocumentRef = groupCollectionRef.document()

            newGroupDocumentRef.set(groupData).await()



            val userDocumentRef = db.collection("users").document(userId)

            if (userDocumentRef.get().await().exists()) {
                userDocumentRef.update("groups", FieldValue.arrayUnion(newGroupDocumentRef.id))
            }

            newGroupDocumentRef.update("users", FieldValue.arrayUnion(userDocumentRef.id))
            DataRequestWrapper(data = Unit)
        } catch (e: Exception) {
            Log.d("ADD_GROUP_RESPONSE", e.stackTraceToString())
            DataRequestWrapper(exception = e)
        }
    }

    suspend fun createTransactionForGroup(
        groupId: String, transactionData: Map<String, Any>
    ): DataRequestWrapper<Transaction, String, Exception> {
        return try {

            val groupDocumentRef = firestore.collection("groups").document(groupId)

            val transactionCollectionRef = groupDocumentRef.collection("transactions")
            val newTransactionDocumentRef = transactionCollectionRef.document()


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

    fun setSingleAmounts(
        groupId: String, transactionId: String, singleAmountData: Map<String, Any>
    ) {


        if (hasNonDoubleValues(singleAmountData)) return
        try {
            val transactionDocumentRef =
                firestore.collection("groups").document(groupId).collection("transactions")
                    .document(transactionId)
            val singleAmountCollectionRef = transactionDocumentRef.collection("singleAmount")

            val batch = firestore.batch()

            singleAmountData.forEach { (key, value) ->
                val docRef = singleAmountCollectionRef.document(key)
                val data = mapOf<String, Any>(
                    "amount" to value as Double
                )

                batch.set(docRef, data)
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

    suspend fun savePayPalAddress(
        userId: String,
        paypalAddress: String
    ): DataRequestWrapper<Unit, String, Exception> {
        return try {
            firestore.collection("users").document(userId)
                .update("paypalAddress", paypalAddress)
                .await()
            DataRequestWrapper(data = Unit)
        } catch (e: Exception) {
            DataRequestWrapper(exception = e)
        }
    }

    /********************************************* MUTATIONS UPDATE ****************************************************************************/

    suspend fun updateTransactionInGroup(
        groupId: String, transactionId: String, transactionData: Map<String, Any>
    ): DataRequestWrapper<Transaction, String, Exception> {
        return try {


            Log.d("test_test", "$transactionData, $transactionId, $groupId")

            val groupDocumentRef = firestore.collection("groups").document(groupId)

            val transactionCollectionRef = groupDocumentRef.collection("transactions")
            val transactionDocumentRef = transactionCollectionRef.document(transactionId)


            transactionDocumentRef.update(transactionData).await()

            val getTransaction = transactionDocumentRef.get().await()

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
            Log.d("UPDATE_TRANSACTION_GROUP_RESPONSE", e.stackTraceToString())
            DataRequestWrapper(exception = e)
        }
    }

    /********************************************* MUTATIONS DELETE ****************************************************************************/

    suspend fun deleteTransactionGroup(
        groupId: String, transactionId: String
    ): DataRequestWrapper<Unit, String, Exception> {
        val userId = auth.currentUser?.uid
        return try {
            if (userId != null) {
                firestore.collection("groups").document(groupId).collection("transactions")
                    .document(transactionId).delete().await()

                DataRequestWrapper(data = Unit)
            } else {
                throw Exception("groupId or transactionId is null.")
            }
        } catch (e: Exception) {
            Log.d("DELETE_RESPONSE", e.stackTraceToString())
            DataRequestWrapper(exception = e)
        }
    }
}
