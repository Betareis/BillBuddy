package com.example.myapplication.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.Group
import com.example.myapplication.data.model.User
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.domain.userManagement.ShowProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(private val showProfileUseCase: ShowProfileUseCase) :
    ViewModel() {

    suspend fun getUserProfile(): DataRequestWrapper<User, String, Exception> {
        return showProfileUseCase()
    }
}




