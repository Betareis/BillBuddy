package com.example.myapplication.ui.screens.transactions

import com.example.myapplication.data.repository.FirestoreRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Group
import com.example.myapplication.data.model.Transaction
import com.example.myapplication.data.model.User
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.domain.balanceManagement.GetBalancesGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val getBalancesGroupUseCase: GetBalancesGroupUseCase
) :
    ViewModel() {

    suspend fun getGroupTransactionsFirestore(groupId: String): DataRequestWrapper<MutableList<Transaction>, String, Exception> {
        return firestoreRepository.getTransactionsGroup(groupId)
    }

    suspend fun getBalancesGroup(
        groupId: String
    ): DataRequestWrapper<Map<User, Double>, String, Exception> {
        return getBalancesGroupUseCase(groupId)
    }


    suspend fun getUsernameById(userId: String): DataRequestWrapper<String, String, Exception> {
        return firestoreRepository.getUsername(userId)
    }
}
