package com.prax19.budgetguard.app.android.repository

import android.util.Log
import com.prax19.budgetguard.app.android.api.BudgetGuardApi
import com.prax19.budgetguard.app.android.data.auth.AuthResult
import com.prax19.budgetguard.app.android.data.dto.BudgetDTO
import com.prax19.budgetguard.app.android.data.model.Budget
import com.prax19.budgetguard.app.android.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class BudgetRepository @Inject constructor(
    private val api: BudgetGuardApi,
    private val authRepository: AuthRepository,
    private val operationRepository: OperationRepository
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
                operationRepository.authenticate()
            }
            is AuthResult.Unauthorized -> {
            }
            is AuthResult.Error -> {

            }
        }
    }

    suspend fun getAllBudgets() : Resource<List<Budget>> {
        val response: List<Budget>
        try {
            response = api.getAllBudgets(token).map {
                Budget(
                    it.id,
                    it.name,
                    it.ownerId,
                    emptyList()
                )
            }
            response.map {
                it.copy(operations = operationRepository.getAllOperations(it).data!!)
            }
        } catch (e: Exception) {
            Log.e("BudgetRepository", "getAllBudgets: ", e)
            return Resource.Error()
        }
        return Resource.Success(response)
    }

    suspend fun getBudget(budgetId: Long): Resource<Budget> {
        val response: Budget
        try {
            response = api.getBudget(token, budgetId).let {
                Budget(
                    it.id,
                    it.name,
                    it.ownerId,
                    emptyList()
                )
            }
            response.let {
                it.copy(operations = operationRepository.getAllOperations(it).data!!)
            }
        } catch (e: Exception) {
            Log.e("BudgetRepository", "getBudget: ", e)
            return Resource.Error()
        }
        return Resource.Success(response)
    }

    suspend fun postBudget(budget: Budget): Resource<String> {
        try {
            api.postBudget(token,
                BudgetDTO(
                    -1,
                    budget.name,
                    budget.ownerId,
                    emptyList()
                )
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
            api.putBudget(token, budget.id,
                BudgetDTO(
                    budget.id,
                    budget.name,
                    budget.ownerId,
                    emptyList() // operation list managed in OperationRepository
                )
            )
        } catch (e: Exception) {
            Log.e("BudgetRepository", "putBudget: ", e)
            return Resource.Error()
        }
        return Resource.Success("Budget successfully updated!")
    }

    suspend fun deleteBudget(budget: Budget): Resource<String> {
        try {
            api.deleteBudget(token, budget.id)
        } catch (e: Exception) {
            Log.e("BudgetRepository", "deleteBudget: ", e)
            return Resource.Error()
        }
        return Resource.Success("Budget successfully deleted!")
    }

}