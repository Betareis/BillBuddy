package com.example.myapplication.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Group
import com.example.myapplication.data.model.User
import com.example.myapplication.data.repository.FirestoreRepository
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.domain.userManagement.ShowProfileUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val showProfileUseCase: ShowProfileUseCase,
    private val firestoreRepository: FirestoreRepository
) :
    ViewModel() {

    suspend fun getUserProfile(): DataRequestWrapper<User, String, Exception> {
        return showProfileUseCase()
    }

    fun savePayPalAddress(paypalAddress: String) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val result = firestoreRepository.savePayPalAddress(userId, paypalAddress)
            if (result.exception != null) {
            }
        }
    }
}




