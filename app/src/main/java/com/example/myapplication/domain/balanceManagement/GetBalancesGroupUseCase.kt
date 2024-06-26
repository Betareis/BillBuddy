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

            for (balance in process2.data!!) {
                val userId = balance.id ?: continue
                val user = process1.data!!.find { it.id == userId }
                    ?: continue

                userBalanceMap[user] = balance.balance
            }

            DataRequestWrapper(data = userBalanceMap.toMap())

        } catch (e: Exception) {
            DataRequestWrapper(exception = e)
        }


    }
}