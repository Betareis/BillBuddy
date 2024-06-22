package com.example.myapplication.domain.balanceManagement

import android.util.Log
import com.example.myapplication.data.repository.FirestoreRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CalculateBalancesUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {

    suspend operator fun invoke(groupId: String) {

        // Transactions aus jeweiliger Gruppe bekommen
        val transactions = try {
            firestoreRepository.getTransactionsGroup(groupId).data!!

        } catch (e: Exception) {
            null // Return empty list on error
        }

        for (transaction in transactions!!) {
            val amount: Double = transaction.amount - firestoreRepository.getSingleAmount(
                groupId, transaction.id!!, transaction.payedBy
            ).data!!
            firestoreRepository.setBalanceForUserInGroup(
                transaction.payedBy, groupId, amount
            )
        }
    }

    // Abfragen wer welche Transaction bezahlt hat (payedBy)
    // Abfragen wer, mit welchem single amount an der transaction beteiligt war
    // Für jeden Nutzer der Gruppe ausrechnen, wie seine eigene Balance nach allen transaktionen in der gruppe ist
}

