package com.example.myapplication.domain.transactionManagement

import com.example.myapplication.data.repository.FirestoreRepository
import com.example.myapplication.data.wrappers.DataRequestWrapper
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {
    suspend operator fun invoke(
        groupId: String,
        transactionId: String
    ): DataRequestWrapper<Unit, String, Exception> {


        return firestoreRepository.deleteTransactionGroup(groupId, transactionId)
    }
}