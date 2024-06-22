package com.example.myapplication.ui.screens.transactions

import com.example.myapplication.data.repository.FirestoreRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Group
import com.example.myapplication.data.model.Transaction
import com.example.myapplication.data.model.User
import com.example.myapplication.data.wrappers.DataRequestWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(private val firestoreRepository: FirestoreRepository) :
    ViewModel() {

    suspend fun getGroupTransactionsFirestore(groupId: String): DataRequestWrapper<MutableList<Transaction>, String, Exception> {
        return firestoreRepository.getTransactionsGroup(groupId)
    }

    suspend fun getUsersOfGroup(
        groupId: String
    ): DataRequestWrapper<MutableList<User>, String, Exception> {
        return firestoreRepository.getUsersOfGroup(groupId)
    }

    /*
    suspend fun getUserBalances(
        groupId: String
    ): DataRequestWrapper<Map<User, Double>, String, Exception> {
        return firestoreRepository.getUserBalances(groupId)
    }
    */
}
