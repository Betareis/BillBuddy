package com.example.myapplication.domain.balanceManagement

import android.util.Log
import com.example.myapplication.data.repository.FirestoreRepository
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ResetCalculateBalancesUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {
    suspend operator fun invoke(
        groupId: String, transactionId: String,
    ): DataRequestWrapper<Unit, String, Exception> {

        return try {
            val transaction = firestoreRepository.getSingleTransaction(transactionId, groupId).data
            val getSingleAmounts = firestoreRepository.getSingleAmounts(groupId, transactionId)

            for (singleAmount in getSingleAmounts.data!!) {
                Log.d("singleAmount", singleAmount.toString())
                if (transaction!!.payedBy == singleAmount.id) {
                    val amount: Double = transaction.amount - singleAmount.amount
                    firestoreRepository.setBalanceForUserInGroup(
                        transaction.payedBy, groupId, -amount
                    )
                } else {
                    val debt: Double = singleAmount.amount
                    firestoreRepository.setBalanceForUserInGroup(
                        singleAmount.id.toString(), groupId, debt
                    )
                }
            }
            DataRequestWrapper(data = Unit)
        } catch (e: Exception) {
            DataRequestWrapper(exception = e)
        }
    }
}