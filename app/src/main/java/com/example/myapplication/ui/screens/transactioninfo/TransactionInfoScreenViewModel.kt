package com.example.myapplication.ui.screens.transactioninfo

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.domain.balanceManagement.ResetCalculateBalancesUseCase
import com.example.myapplication.domain.transactionManagement.DeleteTransactionUseCase
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