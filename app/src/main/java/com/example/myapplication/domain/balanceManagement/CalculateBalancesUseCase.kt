package com.example.myapplication.domain.balanceManagement

import com.example.myapplication.data.repository.FirestoreRepository
import javax.inject.Inject

class CalculateBalancesUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {
    // Transactions aus jeweiliger Gruppe bekommen
    // Abfragen wer welche Transaction bezahlt hat (payedBy)
    // Abfragen wer, mit welchem single amount an der transaction beteiligt war
    // Für jeden Nutzer der Gruppe ausrechnen, wie seine eigene Balance nach allen transaktionen in der gruppe ist
}