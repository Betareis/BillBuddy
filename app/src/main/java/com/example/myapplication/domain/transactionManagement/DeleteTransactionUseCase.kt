package com.example.myapplication.domain.transactionManagement

import com.example.myapplication.data.repository.FirestoreRepository
import com.example.myapplication.data.wrappers.DataRequestWrapper
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {
    suspend operator fun invoke(
        groupId: String,
        transactionId: String
    ): DataRequestWrapper<Unit, String, Exception> {

// Todo: Exceptions are missing here if the attributes are not defined or the transaction cannot be found
        //Todo: | Optional | check if the user is the member of the group

        return firestoreRepository.deleteTransactionGroup(groupId, transactionId)
    }
}