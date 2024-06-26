package com.example.myapplication.ui.screens.newentry

import com.example.myapplication.data.repository.FirestoreRepository
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.User
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.domain.balanceManagement.CalculateBalancesUseCase
import com.example.myapplication.domain.transactionManagement.AddTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewEntryScreenViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val calculateBalancesUseCase: CalculateBalancesUseCase
) : ViewModel() {
    suspend fun addTransactionForGroup(
        groupId: String, transactionData: Map<String, Any>, singleAmountData: Map<String, Any>
    ): DataRequestWrapper<Unit, String, Exception> {
        return try {
            val process1 = addTransactionUseCase(groupId, transactionData, singleAmountData)
            if (process1.exception != null) throw Exception("Error add a transaction")
            if (process1.data == null || process1.data!!.id?.isBlank() == true || process1.data!!.id == null) {
                return DataRequestWrapper(exception = Exception("Error in process 1 addTransaction"))
            }
            val process2 = calculateBalancesUseCase(groupId, transactionId = process1.data!!.id.toString())
            if (process2.exception != null) throw Exception(process2.exception)
            DataRequestWrapper(data = Unit)
        } catch (e: Exception) {
            DataRequestWrapper(exception = e)
        }
    }
    suspend fun getUsersOfGroup(
        groupId: String
    ): DataRequestWrapper<MutableList<User>, String, Exception> {
        return firestoreRepository.getUsersOfGroup(groupId)
    }
}