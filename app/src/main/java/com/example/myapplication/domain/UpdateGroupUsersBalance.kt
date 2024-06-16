package com.example.myapplication.domain

import com.example.myapplication.data.repository.FirestoreRepository
import javax.inject.Inject

class UpdateGroupUsersBalance @Inject constructor(private val firestoreRepository: FirestoreRepository) {
}