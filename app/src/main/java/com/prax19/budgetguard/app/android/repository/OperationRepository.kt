package com.prax19.budgetguard.app.android.repository

import com.prax19.budgetguard.app.android.api.BudgetGuardApi
import com.prax19.budgetguard.app.android.data.dto.BudgetOperationDTO
import com.prax19.budgetguard.app.android.data.model.Budget
import com.prax19.budgetguard.app.android.data.model.Operation
import com.prax19.budgetguard.app.android.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class OperationRepository @Inject constructor(
    private val api: BudgetGuardApi
) {

    //TODO: handle user
    private val auth = "Basic cGF0cnlrLnBpcm9nQG8zNjUudXMuZWR1LnBsOnBhc3N3b3Jk"

    suspend fun getAllOperations(budget: Budget) : Resource<List<Operation>> {
        val response = try {
            api.getBudgetOperations(auth, budget.id).map {
                Operation(
                    it.id,
                    it.name,
                    budget,
                    it.userId,
                    it.value
                )
            }

        } catch (e: Exception) {
            return Resource.Error("An error occurred during loading list of BudgetOperations.")
        }
        return Resource.Success(response)
    }

    suspend fun postOperation(budget: Budget, operation: Operation): Resource<Budget> {
        val response: Budget
        try {
            api.postBudgetOperation(auth, budget.id,
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
            return Resource.Error("An error occurred during posting the BudgetOperation.")
        }
        return Resource.Success(response)
    }

    suspend fun putOperations(budget: Budget, operation: Operation) : Resource<String> {
        val response: Budget
        try {
            if(operation.id < 0)
                throw Exception("invalid BudgetOperation id")
            api.putOperation(auth, budget.id, operation.id,
                BudgetOperationDTO(
                    operation.id,
                    operation.name,
                    budget.id,
                    operation.userId,
                    operation.value
                )
            )
        } catch (e: Exception) {
            return Resource.Error("An error occurred during updating BudgetOperation.")
        }
        return Resource.Success("BudgetOperation successfully updated!")
    }

    suspend fun deleteOperation(budget: Budget, operation: Operation) : Resource<String> {
        try {
            api.deleteOperation(auth, budget.id, operation.id)
        } catch (e: Exception) {
            return Resource.Error("An error occurred during deleting BudgetOperation.")
        }
        return Resource.Success("BudgetOperation successfully deleted!")
    }

}