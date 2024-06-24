package com.example.myapplication.domain.transactionManagement

import androidx.core.text.isDigitsOnly
import com.example.myapplication.data.repository.FirestoreRepository
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.google.protobuf.Empty
import javax.inject.Inject


fun convertMapValues(transactionData: Map<String, Any>): Map<String, Any> {
    return mapOf<String, Any>(
        "name" to transactionData["name"].toString(),
        "amount" to transactionData["amount"].toString().toDouble(),
        "payedBy" to transactionData["payedBy"].toString(),
        "date" to transactionData["date"].toString()
    )
}

fun hasBlankValues(map: Map<String, Any>): Boolean {
    return map.values.any { it.toString().isBlank() }
}

class AddTransactionUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {

    suspend operator fun invoke(
        groupId: String,
        transactionData: Map<String, Any>,
        singleAmountData: Map<String, Any>,
    ): DataRequestWrapper<Unit, String, Exception> {
        validateTransactionData(transactionData)

        return try {
            val transaction = firestoreRepository.createTransactionForGroup(
                groupId, convertMapValues(transactionData)
            ).data

            if (transaction?.id.isNullOrEmpty()) {
                throw Exception("Transaction creation failed")
            }

            firestoreRepository.setSingleAmounts(
                groupId, transactionId = transaction!!.id!!, singleAmountData
            )
            DataRequestWrapper(data = Unit)

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

/*class AddTransactionUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {
    suspend operator fun invoke(
        groupId: String,
        transactionData: Map<String, Any>,
        singleAmountData: Map<String, Any>,
    ): DataRequestWrapper<Unit, String, Exception> {
        if (groupId.isBlank()) {
            return DataRequestWrapper(null, null, Exception("Group cannot be found"))
            //throw Exception("Group cannot be found")
        } else if (transactionData["name"].toString()
                .isBlank() && transactionData["amount"].toString()
                .isBlank() && transactionData["payedBy"].toString()
                .isBlank() && transactionData["date"].toString().isBlank()
        ) {
            //throw Exception("Please fill out the form")
            return DataRequestWrapper(null, null, Exception("Please fill out the form"))
        }

        //Todo: maybe no special characters and numbers
        else if (transactionData["name"].toString().isBlank() || transactionData["name"].toString()
                .isDigitsOnly()
        ) {
            return DataRequestWrapper(
                null, null, Exception("Please enter a valid transaction name")
            )
        } else if (transactionData["payedBy"].toString().isBlank()) {
            return DataRequestWrapper(
                null, null, Exception("Please select a payedBy user")
            )
        } else if (transactionData["date"].toString().isBlank()) {
            return DataRequestWrapper(
                null, null, Exception("Please select a date")
            )
        }

        //! Try catch, because toDouble or isDouble cannot directly used in if conditions without causing the app to crash

        try {
            val amountDouble = transactionData["amount"].toString().trim().toDoubleOrNull()
            if (transactionData["amount"].toString().isBlank() || amountDouble == null) {
                return DataRequestWrapper(
                    null, null, Exception("Please enter a valid transaction amount")
                )
            }
        } catch (e: Exception) {
            return DataRequestWrapper(
                null, null, Exception("Please enter a valid transaction amount")
            )
        }

        return try {
            val transaction = firestoreRepository.createTransactionForGroup(
                groupId, convertMapValues(transactionData)
            ).data

            val transactionId = transaction!!.id


            if (transactionId!!.isEmpty()) {
                throw Exception("Transaction Data is empty")
            } else {
                firestoreRepository.setSingleAmounts(
                    groupId, transactionId = transactionId, singleAmountData
                )
            }
            DataRequestWrapper(data = Unit)

        } catch (e: Exception) {
            DataRequestWrapper(exception = e)
        }
    }
}*/