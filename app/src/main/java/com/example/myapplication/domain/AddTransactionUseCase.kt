package com.example.myapplication.domain

import com.example.myapplication.data.repository.FirestoreRepository
import com.example.myapplication.data.wrappers.DataRequestWrapper
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {
    suspend operator fun invoke(groupId: String, transactionData: Map<String, Any>): DataRequestWrapper<Unit, String, Exception> {
        if (groupId.isBlank()) {
            return DataRequestWrapper(null, null, Exception("Group cannot be found"))
            //throw Exception("Group cannot be found")
        }
        if (transactionData["name"].toString().isEmpty() && transactionData["amount"].toString().isEmpty()) {
            //throw Exception("Please fill out the form")
            return DataRequestWrapper(null, null, Exception("Please fill out the form"))
        }
        if (transactionData["name"].toString().isEmpty()) {
            //throw Exception("Please enter a transaction name")
            return DataRequestWrapper(null, null, Exception("Please enter a transaction name"))
        }
        if (transactionData["amount"].toString().isEmpty()) {
            //throw Exception("Please enter a discovery date in the past")
            return DataRequestWrapper(null, null, Exception("Please enter a transaction amount"))
        }
        return firestoreRepository.createTransactionForGroup(groupId, transactionData)
    }
}