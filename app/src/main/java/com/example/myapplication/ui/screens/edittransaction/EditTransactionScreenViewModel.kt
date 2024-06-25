package com.example.myapplication.ui.screens.edittransaction

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.User
import com.example.myapplication.data.repository.FirestoreRepository
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.domain.balanceManagement.CalculateBalancesUseCase
import com.example.myapplication.domain.transactionManagement.DeleteTransactionUseCase
import com.example.myapplication.domain.transactionManagement.UpdateTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditTransactionScreenViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val calculateBalancesUseCase: CalculateBalancesUseCase
) : ViewModel() {
    suspend fun deleteTransaction(
        groupId: String, transactionId: String
    ): DataRequestWrapper<Unit, String, Exception> {
        return deleteTransactionUseCase(groupId, transactionId)
    }

    suspend fun updateSpecificTransactionGroup(
        groupId: String,
        transactionId: String,
        transactionData: Map<String, Any>,
        singleAmountData: Map<String, Any>
    ): DataRequestWrapper<Unit, String, Exception> {
        return try {
            val process1 = updateTransactionUseCase.invoke(
                groupId,
                transactionId,
                transactionData,
                singleAmountData
            )
            if (process1.exception != null) throw Exception("Error update transaction $transactionId")
            if (process1.data == null || process1.data!!.id?.isBlank() == true || process1.data!!.id == null) {
                return DataRequestWrapper(exception = Exception("Error in process 1 updateTransaction"))
            }
            val process2 =
                calculateBalancesUseCase(groupId, transactionId = process1.data!!.id.toString())
            if (process2.exception != null) throw Exception(process2.exception)
            DataRequestWrapper(data = Unit)
        } catch (e: Exception) {
            DataRequestWrapper(exception = e)
        }
        //return addTransactionUseCase(groupId, transactionData, singleAmountData)
    }

    suspend fun getUsersOfGroup(
        groupId: String
    ): DataRequestWrapper<MutableList<User>, String, Exception> {
        return firestoreRepository.getUsersOfGroup(groupId)
    }
}