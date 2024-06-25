package com.example.myapplication.domain.balanceManagement

import android.util.Log
import com.example.myapplication.data.repository.FirestoreRepository
import com.example.myapplication.data.wrappers.DataRequestWrapper
import javax.inject.Inject

class CalculateBalancesUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {

    suspend operator fun invoke(
        groupId: String, transactionId: String
    ): DataRequestWrapper<Unit, String, Exception> {

        // Transactions aus jeweiliger Gruppe bekommen
        /*val transactions = try {
            firestoreRepository.getTransactionsGroup(groupId).data!!*/

        /*} catch (e: Exception) {
            return DataRequestWrapper(exception = Exception("No Transactions found"))
            //null // Return empty list on error
        }*/

        return try {
            val transaction = firestoreRepository.getSingleTransaction(transactionId, groupId).data
            val getSingleAmounts = firestoreRepository.getSingleAmounts(groupId, transactionId)

            for (singleAmount in getSingleAmounts.data!!) {
                Log.d("singleAmount", singleAmount.toString())
                if (transaction!!.payedBy == singleAmount.id) {
                    val amount: Double = transaction.amount - singleAmount.amount
                    firestoreRepository.setBalanceForUserInGroup(
                        transaction.payedBy, groupId, amount
                    )
                } else {
                    val debt: Double = -singleAmount.amount
                    firestoreRepository.setBalanceForUserInGroup(
                        singleAmount.id.toString(), groupId, debt
                    )
                }

                /*val amount: Double = transaction.amount - singleAmount.amount

                firestoreRepository.setBalanceForUserInGroup(
                    transaction.payedBy, groupId, amount
                )*/
            }


            /*val amount: Double = transaction!!.amount - firestoreRepository.getSingleAmount(
                groupId, transaction.id!!, transaction.payedBy
            ).data!!
            Log.d("test_amount", amount.toString())

            firestoreRepository.setBalanceForUserInGroup(
                transaction.payedBy, groupId, amount
            )*/
            DataRequestWrapper(data = Unit)
        } catch (e: Exception) {
            DataRequestWrapper(exception = e)
        }
    }
}

/*for (transaction in transactions) {
                Log.d(
                    "blablu: ",
                    "groupId: " + groupId + ", transaction_id" + transaction.id + " ,payedBy:" + transaction.payedBy + ", amount:" + transaction.amount
                )


                val amount: Double = transaction.amount - firestoreRepository.getSingleAmount(
                    groupId, transaction.id!!, transaction.payedBy
                ).data!!
                Log.d("test_amount", amount.toString())

                firestoreRepository.setBalanceForUserInGroup(
                    transaction.payedBy, groupId, amount
                )
            }*/

// Abfragen wer welche Transaction bezahlt hat (payedBy)
// Abfragen wer, mit welchem single amount an der transaction beteiligt war
// Für jeden Nutzer der Gruppe ausrechnen, wie seine eigene Balance nach allen transaktionen in der gruppe ist

