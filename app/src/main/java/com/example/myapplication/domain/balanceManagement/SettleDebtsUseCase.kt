package com.example.myapplication.domain.balanceManagement

import com.example.myapplication.data.repository.FirestoreRepository
import javax.inject.Inject

class SettleDebtsUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {
}