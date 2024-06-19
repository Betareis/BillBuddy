package com.example.myapplication.ui.screens.edittransaction

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.repository.FirestoreRepository
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.domain.transactionManagement.DeleteTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditTransactionScreenViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {
    suspend fun deleteTransaction(
        groupId: String, transactionId: String
    ): DataRequestWrapper<Unit, String, Exception> {
        return deleteTransactionUseCase(groupId, transactionId)
    }
}