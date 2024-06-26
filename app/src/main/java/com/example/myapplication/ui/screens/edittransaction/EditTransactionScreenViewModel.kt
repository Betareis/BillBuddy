package com.example.myapplication.ui.screens.edittransaction

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.User
import com.example.myapplication.data.repository.FirestoreRepository
import com.example.myapplication.data.wrappers.DataRequestWrapper
import com.example.myapplication.domain.balanceManagement.CalculateBalancesUseCase
import com.example.myapplication.domain.balanceManagement.ResetCalculateBalancesUseCase
import com.example.myapplication.domain.transactionManagement.DeleteTransactionUseCase
import com.example.myapplication.domain.transactionManagement.UpdateTransactionUseCase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditTransactionScreenViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val resetCalculateBalancesUseCase: ResetCalculateBalancesUseCase,
    private val calculateBalancesUseCase: CalculateBalancesUseCase
) : ViewModel() {
    suspend fun deleteTransaction(
        groupId: String, transactionId: String
    ): DataRequestWrapper<Unit, String, Exception> {
        return try {
            resetCalculateBalancesUseCase(groupId, transactionId)
            deleteTransactionUseCase(groupId, transactionId)
        } catch (e: Exception) {
            DataRequestWrapper(exception = e)
        }
    }

    suspend fun updateSpecificTransactionGroup(
        groupId: String,
        transactionId: String,
        transactionData: Map<String, Any>,
        singleAmountData: Map<String, Any>
    ): DataRequestWrapper<Unit, String, Exception> {
        return try {
            val process1 = updateTransactionUseCase(
                groupId, transactionId, transactionData, singleAmountData
            )

            if (process1.exception != null) throw Exception("Error update transaction $transactionId")
            if (process1.data == null || process1.data!!.id?.isBlank() == true || process1.data!!.id == null) {
                return DataRequestWrapper(exception = Exception("Error in process 1 updateTransaction"))
            }

            val process2 = resetCalculateBalancesUseCase(
                groupId, transactionId = transactionId
            )

            if (process2.exception != null) throw Exception("Error reset resetCalculateBalancesUseCase")

            DataRequestWrapper(data = Unit)
        } catch (e: Exception) {
            DataRequestWrapper(exception = e)
        }
        //return addTransactionUseCase(groupId, transactionData, singleAmountData)
    }

    suspend fun getUsersOfGroup(
        groupId: String
    ): DataRequestWrapper<MutableList<User>, String, Exception> {
        return firestoreRepository.getUsersOfGroup(groupId)
    }
}