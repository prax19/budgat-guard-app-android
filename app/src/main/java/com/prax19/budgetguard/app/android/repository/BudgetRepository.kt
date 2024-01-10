package com.prax19.budgetguard.app.android.repository

import com.prax19.budgetguard.app.android.api.BudgetGuardApi
import com.prax19.budgetguard.app.android.data.dto.BudgetDTO
import com.prax19.budgetguard.app.android.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class BudgetRepository @Inject constructor(
    private val api: BudgetGuardApi
) {

    private val auth = "Basic cGF0cnlrLnBpcm9nQG8zNjUudXMuZWR1LnBsOnBhc3N3b3Jk"

    suspend fun getAllBudgets() : Resource<List<BudgetDTO>> {
        val response = try {
            api.getAllBudgets(auth)
        } catch (e: Exception) {
            return Resource.Error("An error occurred during loading list of budgets.")
        }
        return Resource.Success(response)
    }

    suspend fun getBudget(budgetId: Long): Resource<BudgetDTO> {
        val response = try {
            api.getBudget(auth, budgetId)
        } catch (e: Exception) {
            return Resource.Error("An error occurred during loading of budget.")
        }
        return Resource.Success(response)
    }

    suspend fun postBudget(budget: BudgetDTO): Resource<String> {
        try {
            api.postBudget(auth, budget)
        } catch (e: Exception) {
            return Resource.Error("An error occurred during posting the budget.")
        }
        return Resource.Success("Budget successfully created!")
    }

    suspend fun putBudget(budget: BudgetDTO): Resource<String> {
        try {
            if(budget.id < 0)
                throw Exception("invalid Budget id")
            api.putBudget(auth, budget.id, budget)
        } catch (e: Exception) {
            return Resource.Error("An error occurred during posting the budget.")
        }
        return Resource.Success("Budget successfully updated!")
    }

    suspend fun deleteBudget(budget: BudgetDTO): Resource<String> {
        try {
            api.deleteBudget(auth, budget.id)
        } catch (e: Exception) {
            return Resource.Error("An error occurred during posting the budget.")
        }
        return Resource.Success("Budget successfully deleted!")
    }

}