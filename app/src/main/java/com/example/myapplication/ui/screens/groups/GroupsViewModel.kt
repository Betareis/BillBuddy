package com.example.myapplication.ui.screens.groups

import com.example.myapplication.data.repository.FirestoreRepository
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.Group
import com.example.myapplication.data.wrappers.DataRequestWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class GroupsViewModel @Inject constructor(private val firestoreRepository: FirestoreRepository) :
    ViewModel() {

    suspend fun getGroupsFirestore(): DataRequestWrapper<MutableList<Group>, String, Exception> {
        return firestoreRepository.getGroups()
    }

    suspend fun addGroup(groupData: Map<String, Any>) : DataRequestWrapper<Unit, String, Exception> {
        return firestoreRepository.createGroup(groupData)
    }
}


