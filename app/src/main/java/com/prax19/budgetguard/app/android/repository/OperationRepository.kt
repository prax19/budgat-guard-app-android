package com.prax19.budgetguard.app.android.repository

import android.util.Log
import com.prax19.budgetguard.app.android.api.BudgetGuardApi
import com.prax19.budgetguard.app.android.data.auth.AuthResult
import com.prax19.budgetguard.app.android.data.dto.BudgetOperationDTO
import com.prax19.budgetguard.app.android.data.model.Budget
import com.prax19.budgetguard.app.android.data.model.Operation
import com.prax19.budgetguard.app.android.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.HttpException
import java.net.ConnectException
import javax.inject.Inject

@ActivityScoped
class OperationRepository @Inject constructor(
    private val api: BudgetGuardApi
) {
    suspend fun getAllOperations(budget: Budget) : Resource<List<Operation>> {
        val response = try {
            api.getBudgetOperations(budget.id).map {
                Operation(
                    it.id,
                    it.name,
                    budget,
                    it.userId,
                    it.value
                )
            }
        } catch (e: HttpException) {
            Log.e("OperationRepository", "HTTP code: ${e.code()}")
            if(e.code() == 401)
                return Resource.Error(
                    authResult = AuthResult.Unauthorized()
                )
            return Resource.Error()
        } catch (e: ConnectException) {
            Log.e("AuthRepository", "Cannot connect to the server!")
            return Resource.Error(
                authResult = AuthResult.Error("Cannot connect to the server!")
            )
        } catch (e: Exception) {
            Log.e("OperationRepository", "getAllOperations: ", e)
            return Resource.Error()
        }
        return Resource.Success(response)
    }

    suspend fun getOperationById(budget: Budget, id: Long): Resource<Operation> {
        val response = try {
            api.getBudgetOperation(budget.id, id).let {
                Operation(
                    id,
                    it.name,
                    budget,
                    it.userId,
                    it.value
                )
            }
        } catch (e: HttpException) {
            Log.e("OperationRepository", "HTTP code: ${e.code()}")
            if(e.code() == 401)
                return Resource.Error(
                    authResult = AuthResult.Unauthorized()
                )
            if(e.code() == 403)
                return Resource.Error(
                    authResult = AuthResult.Unauthorized()
                )
            return Resource.Error()
        } catch (e: ConnectException) {
            Log.e("AuthRepository", "Cannot connect to the server!")
            return Resource.Error(
                authResult = AuthResult.Error("Cannot connect to the server!")
            )
        } catch (e: Exception) {
            Log.e("OperationRepository", "getOperationById: ", e)
            return Resource.Error()
        }
        return Resource.Success(response)
    }

    suspend fun postOperation(budget: Budget, operation: Operation): Resource<Budget> {
        val response: Budget
        try {
            api.postBudgetOperation(budget.id,
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
        } catch (e: HttpException) {
            Log.e("OperationRepository", "HTTP code: ${e.code()}")
            if(e.code() == 401)
                return Resource.Error(
                    authResult = AuthResult.Unauthorized()
                )
            return Resource.Error()
        } catch (e: ConnectException) {
            Log.e("AuthRepository", "Cannot connect to the server!")
            return Resource.Error(
                authResult = AuthResult.Error("Cannot connect to the server!")
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
            api.putOperation(budget.id, operation.id,
                BudgetOperationDTO(
                    operation.id,
                    operation.name,
                    budget.id,
                    operation.userId,
                    operation.value
                )
            )
        } catch (e: HttpException) {
            Log.e("OperationRepository", "HTTP code: ${e.code()}")
            if(e.code() == 401)
                return Resource.Error(
                    authResult = AuthResult.Unauthorized()
                )
            return Resource.Error()
        } catch (e: ConnectException) {
            Log.e("AuthRepository", "Cannot connect to the server!")
            return Resource.Error(
                authResult = AuthResult.Error("Cannot connect to the server!")
            )
        } catch (e: Exception) {
            Log.e("OperationRepository", "putOperations: ", e)
            return Resource.Error()
        }
        return Resource.Success("BudgetOperation successfully updated!")
    }

    suspend fun deleteOperation(budget: Budget, operation: Operation) : Resource<String> {
        try {
            api.deleteOperation(budget.id, operation.id)
        } catch (e: HttpException) {
            Log.e("OperationRepository", "HTTP code: ${e.code()}")
            if(e.code() == 401)
                return Resource.Error(
                    authResult = AuthResult.Unauthorized()
                )
            return Resource.Error()
        } catch (e: ConnectException) {
            Log.e("AuthRepository", "Cannot connect to the server!")
            return Resource.Error(
                authResult = AuthResult.Error("Cannot connect to the server!")
            )
        } catch (e: Exception) {
            Log.e("OperationRepository", "deleteOperation: ", e)
            return Resource.Error()
        }
        return Resource.Success("BudgetOperation successfully deleted!")
    }

}