package com.prax19.budgetguard.app.android.repository

import android.util.Log
import com.prax19.budgetguard.app.android.api.BudgetGuardApi
import com.prax19.budgetguard.app.android.data.auth.AuthResult
import com.prax19.budgetguard.app.android.data.dto.BudgetOperationDTO
import com.prax19.budgetguard.app.android.data.model.Budget
import com.prax19.budgetguard.app.android.data.model.Operation
import com.prax19.budgetguard.app.android.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class OperationRepository @Inject constructor(
    private val api: BudgetGuardApi,
    private val authRepository: AuthRepository,
) {

    var token = ""

    //TODO: find another way to do this
    //TODO: prevent all methods from being called unauthorised
    fun authenticate() {
        val result = authRepository.authenticate()
        when(result) {
            is AuthResult.Authorized -> {
                //TODO: do this in repository or so
                token = "Bearer ${result.data.toString()}"
            }
            is AuthResult.Unauthorized -> {
            }
            is AuthResult.Error -> {

            }
        }
    }

    suspend fun getAllOperations(budget: Budget) : Resource<List<Operation>> {
        val response = try {
            api.getBudgetOperations(token, budget.id).map {
                Operation(
                    it.id,
                    it.name,
                    budget,
                    it.userId,
                    it.value
                )
            }

        } catch (e: Exception) {
            Log.e("OperationRepository", "getAllOperations: ", e)
            return Resource.Error()
        }
        return Resource.Success(response)
    }

    suspend fun postOperation(budget: Budget, operation: Operation): Resource<Budget> {
        val response: Budget
        try {
            api.postBudgetOperation(token, budget.id,
                BudgetOperationDTO(
                    -1,
                    operation.name,
                    budget.id,
                    budget.ownerId, //TODO: handle user
                    operation.value
                )
            )
            response = budget.copy(
                operations = budget.operations + operation
            )
        } catch (e: Exception) {
            Log.e("OperationRepository", "postOperation: ", e)
            return Resource.Error()
        }
        return Resource.Success(response)
    }

    suspend fun putOperations(budget: Budget, operation: Operation) : Resource<String> {
        val response: Budget
        try {
            if(operation.id < 0)
                throw Exception("invalid BudgetOperation id")
            api.putOperation(token, budget.id, operation.id,
                BudgetOperationDTO(
                    operation.id,
                    operation.name,
                    budget.id,
                    operation.userId,
                    operation.value
                )
            )
        } catch (e: Exception) {
            Log.e("OperationRepository", "putOperations: ", e)
            return Resource.Error()
        }
        return Resource.Success("BudgetOperation successfully updated!")
    }

    suspend fun deleteOperation(budget: Budget, operation: Operation) : Resource<String> {
        try {
            api.deleteOperation(token, budget.id, operation.id)
        } catch (e: Exception) {
            Log.e("OperationRepository", "deleteOperation: ", e)
            return Resource.Error()
        }
        return Resource.Success("BudgetOperation successfully deleted!")
    }

}