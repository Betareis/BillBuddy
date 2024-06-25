package com.example.myapplication.domain.balanceManagement

import com.example.myapplication.data.model.User
import com.example.myapplication.data.repository.FirestoreRepository
import com.example.myapplication.data.wrappers.DataRequestWrapper
import javax.inject.Inject

class GetBalancesGroupUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {
    suspend operator fun invoke(
        groupId: String
    ): DataRequestWrapper<Map<User, Double>, String, Exception> {
        return try {
            val process1 = firestoreRepository.getUsersOfGroup(groupId)
            if (process1.exception != null) throw Exception(process1.exception)
            val process2 = firestoreRepository.getBalancesOfGroup(groupId)
            if (process2.exception != null) throw Exception(process2.exception)

            val userBalanceMap = mutableMapOf<User, Double>()

            // Loop through balances and find corresponding user in the other list
            for (balance in process2.data!!) {
                val userId = balance.id ?: continue  // Skip if balance has no ID
                val user = process1.data!!.find { it.id == userId }
                    ?: continue  // Skip if no user with matching ID

                userBalanceMap[user] = balance.balance  // Add user and balance to the map
            }

            DataRequestWrapper(data = userBalanceMap.toMap()) // Create an immutable map from the mutable map

        } catch (e: Exception) {
            DataRequestWrapper(exception = e)
        }


    }
}