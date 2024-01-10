package com.prax19.budgetguard.app.android.repository

import com.prax19.budgetguard.app.android.api.BudgetGuardApi
import com.prax19.budgetguard.app.android.data.dto.BudgetDTO
import com.prax19.budgetguard.app.android.data.model.Budget
import com.prax19.budgetguard.app.android.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class BudgetRepository @Inject constructor(
    private val api: BudgetGuardApi,
    private val operationRepository: OperationRepository
) {

    //TODO: handle user
    private val auth = "Basic cGF0cnlrLnBpcm9nQG8zNjUudXMuZWR1LnBsOnBhc3N3b3Jk"

    suspend fun getAllBudgets() : Resource<List<Budget>> {
        val response: List<Budget>
        try {
            response = api.getAllBudgets(auth).map {
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
            return Resource.Error("An error occurred during loading list of budgets.")
        }
        return Resource.Success(response)
    }

    suspend fun getBudget(budgetId: Long): Resource<Budget> {
        val response: Budget
        try {
            response = api.getBudget(auth, budgetId).let {
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
            return Resource.Error("An error occurred during loading of budget.")
        }
        return Resource.Success(response)
    }

    suspend fun postBudget(budget: Budget): Resource<String> {
        try {
            api.postBudget(auth,
                BudgetDTO(
                    -1,
                    budget.name,
                    budget.ownerId,
                    emptyList()
                )
            )
        } catch (e: Exception) {
            return Resource.Error("An error occurred during posting the budget.")
        }
        return Resource.Success("Budget successfully created!")
    }

    suspend fun putBudget(budget: Budget): Resource<String> {
        try {
            if(budget.id < 0)
                throw Exception("invalid Budget id")
            api.putBudget(auth, budget.id,
                BudgetDTO(
                    budget.id,
                    budget.name,
                    budget.ownerId,
                    emptyList() // operation list managed in OperationRepository
                )
            )
        } catch (e: Exception) {
            return Resource.Error("An error occurred during posting the budget.")
        }
        return Resource.Success("Budget successfully updated!")
    }

    suspend fun deleteBudget(budget: Budget): Resource<String> {
        try {
            api.deleteBudget(auth, budget.id)
        } catch (e: Exception) {
            return Resource.Error("An error occurred during posting the budget.")
        }
        return Resource.Success("Budget successfully deleted!")
    }

}