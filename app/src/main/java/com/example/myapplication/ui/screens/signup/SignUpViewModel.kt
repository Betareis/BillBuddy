package com.example.myapplication.ui.screens.signup

import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.transactionManagement.AddTransactionUseCase
import com.example.myapplication.domain.userManagement.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {
    suspend fun registerUser(
        userData: Map<String, Any>, onSuccess: () -> Unit
    ) {
        registerUserUseCase(userData, onSuccess = {
            onSuccess()
        })
    }
}