package com.example.myapplication.ui.screens.groups

import android.util.Log
import com.example.myapplication.data.repository.FirestoreRepository
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.Group
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor(private val firestoreRepository: FirestoreRepository) :
    ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _groupsState = MutableStateFlow<List<Group>>(emptyList())
    val groupsState: StateFlow<List<Group>> = _groupsState.asStateFlow()

    suspend fun getGroupsFirestore(): DataRequestWrapper<MutableList<Group>, String, Exception> {
        return try {
            val user = auth.currentUser
            if (user != null) {
                val data = firestoreRepository.getGroups(userId = user.uid).data!!
                if (data.isEmpty()) {
                    _groupsState.value = emptyList<Group>()
                } else {
                    _groupsState.value = data
                }
                firestoreRepository.getGroups(userId = user.uid)
            }
            DataRequestWrapper(exception = Exception("Not logged in"))

        } catch (e: Exception) {
            _groupsState.value = emptyList<Group>()
            return DataRequestWrapper(exception = e)
        }


    }

    suspend fun addGroup(
        groupData: Map<String, Any>, userId: String
    ): DataRequestWrapper<Unit, String, Exception> {
        return firestoreRepository.createGroup(groupData, userId = userId)
    }
}


