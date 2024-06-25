package com.example.myapplication.domain.transactionManagement

import androidx.core.text.isDigitsOnly
import com.example.myapplication.data.model.Transaction
import com.example.myapplication.data.repository.FirestoreRepository
import com.example.myapplication.data.wrappers.DataRequestWrapper
import javax.inject.Inject


private fun convertMapValues(transactionData: Map<String, Any>): Map<String, Any> {
    return mapOf<String, Any>(
        "name" to transactionData["name"].toString(),
        "amount" to transactionData["amount"].toString().toDouble(),
        "payedBy" to transactionData["payedBy"].toString(),
        "date" to transactionData["date"].toString()
    )
}

private fun hasBlankValues(map: Map<String, Any>): Boolean {
    return map.values.any { it.toString().isBlank() }
}

class UpdateTransactionUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {

    suspend operator fun invoke(
        groupId: String,
        transactionId: String,
        transactionData: Map<String, Any>,
        singleAmountData: Map<String, Any>,
    ): DataRequestWrapper<Transaction, String, Exception> {
        validateTransactionData(transactionData)

        return try {

            val transaction = firestoreRepository.updateTransactionInGroup(groupId, transactionId, convertMapValues(transactionData)).data

            if (transaction?.id.isNullOrEmpty()) {
                throw Exception("Transaction update failed")
            }

            firestoreRepository.setSingleAmounts(
                groupId, transactionId = transaction!!.id!!, singleAmountData
            )
            DataRequestWrapper(data = transaction)

        } catch (e: Exception) {
            DataRequestWrapper(exception = e)
        }
    }

    private fun validateTransactionData(data: Map<String, Any>) {
        if (hasBlankValues(data) || lacksRequiredFields(data) || hasInvalidName(data) || data["amount"].toString()
                .isBlank() || !isDouble(data["amount"].toString())
        ) {
            throw Exception("Invalid transaction data")
        }
    }

    private fun lacksRequiredFields(data: Map<String, Any>) =
        data["name"].toString().isBlank() || data["payedBy"].toString()
            .isBlank() || data["date"].toString().isBlank()

    private fun hasInvalidName(data: Map<String, Any>) =
        data["name"].toString().isBlank() || data["name"].toString().isDigitsOnly()

    private fun isDouble(value: String): Boolean {
        return try {
            value.trim().toDoubleOrNull() != null
        } catch (e: Exception) {
            false
        }
    }
}