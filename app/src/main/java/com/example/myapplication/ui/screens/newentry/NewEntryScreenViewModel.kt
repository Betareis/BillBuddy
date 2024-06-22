package com.example.myapplication.ui.screens.newentry


import com.example.myapplication.data.repository.FirestoreRepository
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.User
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.domain.transactionManagement.AddTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewEntryScreenViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val addTransactionUseCase: AddTransactionUseCase
) : ViewModel() {
    suspend fun addTransactionForGroup(
        groupId: String, transactionData: Map<String, Any>, singleAmountData: Map<String, Any>
    ): DataRequestWrapper<Unit, String, Exception> {
        return addTransactionUseCase(groupId, transactionData, singleAmountData)
    }

    suspend fun getUsersOfGroup(
        groupId: String
    ): DataRequestWrapper<MutableList<User>, String, Exception> {
        return firestoreRepository.getUsersOfGroup(groupId)
    }
}


//Todo: Should be deleted in production
/*suspend fun addTransactionForGroup(
    groupId: String,
    transactionData: Map<String, Any>
): DataRequestWrapper<Unit, String, Exception> {
    return firestoreRepository.createTransactionForGroup(groupId, transactionData)
}*/