package com.example.myapplication.ui.screens.groups

import com.example.myapplication.data.repository.FirestoreRepository
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.Group
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor(private val firestoreRepository: FirestoreRepository) :
    ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun getGroupsFirestore(): DataRequestWrapper<MutableList<Group>, String, Exception> {
        val user = auth.currentUser
        if (user != null) {
            return firestoreRepository.getGroups(userId = user.uid)
        }
        return DataRequestWrapper(exception = Exception("Not logged in"))
    }

    suspend fun addGroup(groupData: Map<String, Any>): DataRequestWrapper<Unit, String, Exception> {
        return firestoreRepository.createGroup(groupData)
    }
}


