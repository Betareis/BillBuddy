package com.example.myapplication.ui.screens.newentry


import com.example.myapplication.data.repository.FirestoreRepository
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.wrappers.DataRequestWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class NewEntryScreenViewModel @Inject constructor(private val firestoreRepository: FirestoreRepository) :
    ViewModel() {
    suspend fun addTransactionForGroup(groupId: String, transactionData: Map<String, Any>) : DataRequestWrapper<Unit, String, Exception> {
        return firestoreRepository.createTransactionForGroup(groupId, transactionData)
    }
}