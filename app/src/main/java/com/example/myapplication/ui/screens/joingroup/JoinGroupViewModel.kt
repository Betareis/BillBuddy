package com.example.myapplication.ui.screens.joingroup

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.repository.FirestoreRepository
import com.example.myapplication.data.wrappers.DataRequestWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JoinGroupViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
) : ViewModel() {

    suspend fun addUserToGroup(
        userId: String, groupId: String
    ): DataRequestWrapper<Unit, String, Exception> {
        return firestoreRepository.joinGroup(userId, groupId)
    }
}