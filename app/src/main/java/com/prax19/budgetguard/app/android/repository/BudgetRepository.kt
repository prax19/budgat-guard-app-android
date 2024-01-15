package com.prax19.budgetguard.app.android.repository

import android.util.Log
import com.prax19.budgetguard.app.android.api.BudgetGuardApi
import com.prax19.budgetguard.app.android.data.auth.AuthResult
import com.prax19.budgetguard.app.android.data.dto.BudgetDTO
import com.prax19.budgetguard.app.android.data.model.Budget
import com.prax19.budgetguard.app.android.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.HttpException
import java.net.ConnectException
import javax.inject.Inject

@ActivityScoped
class BudgetRepository @Inject constructor(
    private val api: BudgetGuardApi,
    private val operationRepository: OperationRepository
) {
    suspend fun getAllBudgets() : Resource<List<Budget>> {
        val response: List<Budget>
        try {
            response = api.getAllBudgets().map {
                Budget(
                    it.id,
                    it.name,
                    it.ownerId,
                    emptyList(),
                    it.balance
                )
            }
            response.map {
                it.copy(operations = operationRepository.getAllOperations(it).data!!)
            }
        } catch (e: HttpException) {
            Log.e("BudgetRepository", "HTTP code: ${e.code()}")
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
            Log.e("BudgetRepository", "getAllBudgets: ", e)
            return Resource.Error()
        }
        return Resource.Success(response)
    }

    suspend fun getBudget(budgetId: Long): Resource<Budget> {
        val response: Budget
        try {
            response = api.getBudget(budgetId).let {
                Budget(
                    it.id,
                    it.name,
                    it.ownerId,
                    emptyList(),
                    it.balance
                )
            }
            response.let {
                it.copy(operations = operationRepository.getAllOperations(it).data!!)
            }
        } catch (e: HttpException) {
            Log.e("BudgetRepository", "HTTP code: ${e.code()}")
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
            Log.e("BudgetRepository", "getBudget: ", e)
            return Resource.Error()
        }
        return Resource.Success(response)
    }

    suspend fun postBudget(budget: Budget): Resource<String> {
        try {
            api.postBudget(
                BudgetDTO(
                    -1,
                    budget.name,
                    budget.ownerId,
                    emptyList()
                )
            )
        } catch (e: HttpException) {
            Log.e("BudgetRepository", "HTTP code: ${e.code()}")
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
            Log.e("BudgetRepository", "postBudget: ", e)
            return Resource.Error()
        }
        return Resource.Success("Budget successfully created!")
    }

    suspend fun putBudget(budget: Budget): Resource<String> {
        try {
            if(budget.id < 0)
                throw Exception("invalid Budget id")
            api.putBudget(budget.id,
                BudgetDTO(
                    budget.id,
                    budget.name,
                    budget.ownerId,
                    emptyList() // operation list managed in OperationRepository
                )
            )
        } catch (e: HttpException) {
            Log.e("BudgetRepository", "HTTP code: ${e.code()}")
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
            Log.e("BudgetRepository", "putBudget: ", e)
            return Resource.Error()
        }
        return Resource.Success("Budget successfully updated!")
    }

    suspend fun deleteBudget(budget: Budget): Resource<String> {
        try {
            api.deleteBudget(budget.id)
        } catch (e: HttpException) {
            Log.e("BudgetRepository", "HTTP code: ${e.code()}")
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
            Log.e("BudgetRepository", "deleteBudget: ", e)
            return Resource.Error()
        }
        return Resource.Success("Budget successfully deleted!")
    }

}