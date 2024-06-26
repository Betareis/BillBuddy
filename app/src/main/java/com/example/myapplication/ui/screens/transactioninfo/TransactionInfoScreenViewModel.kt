package com.example.myapplication.ui.screens.transactioninfo

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.User
import com.example.myapplication.data.repository.FirestoreRepository
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.domain.balanceManagement.CalculateBalancesUseCase
import com.example.myapplication.domain.balanceManagement.ResetCalculateBalancesUseCase
import com.example.myapplication.domain.transactionManagement.DeleteTransactionUseCase
import com.example.myapplication.domain.transactionManagement.UpdateTransactionUseCase
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionInfoScreenViewModel @Inject constructor(
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val resetCalculateBalancesUseCase: ResetCalculateBalancesUseCase,
) : ViewModel() {
    suspend fun deleteTransaction(
        groupId: String, transactionId: String
    ): DataRequestWrapper<Unit, String, Exception> {
        return try {
            resetCalculateBalancesUseCase(groupId, transactionId)
            deleteTransactionUseCase(groupId, transactionId)
        } catch (e: Exception) {
            DataRequestWrapper(exception = e)
        }
    }
}