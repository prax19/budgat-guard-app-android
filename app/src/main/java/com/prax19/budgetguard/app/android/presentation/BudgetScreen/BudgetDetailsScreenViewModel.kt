package com.prax19.budgetguard.app.android.presentation.BudgetScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prax19.budgetguard.app.android.api.BudgetGuardApi
import com.prax19.budgetguard.app.android.data.dto.BudgetOperationDTO
import com.prax19.budgetguard.app.android.data.model.Budget
import com.prax19.budgetguard.app.android.data.model.Operation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetDetailsScreenViewModel @Inject constructor(
    private val api: BudgetGuardApi,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val auth = "Basic cGF0cnlrLnBpcm9nQG8zNjUudXMuZWR1LnBsOnBhc3N3b3Jk"

    private val _budgetState = mutableStateOf(BudgetState())
    val budgetState: State<BudgetState> = _budgetState

    private val budgetId: Long? = null

    init {
        savedStateHandle.get<Long>("budgetId")?.let { budgetId ->
            if(budgetId != -1L) {
                viewModelScope.launch {
                    getBudget(budgetId)
                }
            }
        }
    }

    fun getBudget(budgetId: Long) {
        viewModelScope.launch {
            try {
                _budgetState.value = budgetState.value.copy(isLoading = true)

                val budgetDTO = api.getBudget(
                    auth,
                    budgetId
                )
                val operationsDTO: List<BudgetOperationDTO> = api.getBudgetOperations(
                    auth,
                    budgetId
                )
                val budget = Budget(
                    budgetDTO.id,
                    budgetDTO.name,
                    budgetDTO.ownerId,
                    emptyList()
                )
                val operations: List<Operation> = operationsDTO.map { operation ->
                    Operation(
                        operation.id,
                        operation.name,
                        budget,
                        operation.userId,
                        operation.value
                    )
                }
                budget.operations = operations


                _budgetState.value = budgetState.value.copy(
                    budget = budget,
                    isLoading = false
                )
            } catch (e: Exception) {
                Log.e("BudgetDetailsScreenViewModel", "getBudget: ", e)
                _budgetState.value = budgetState.value.copy(isLoading = false)
            }
        }
    }

    fun createOperation(operation: Operation) {
        try {
            budgetState.value.budget?.let {
                val operationDTO = BudgetOperationDTO(
                    -1,
                    operation.name,
                    operation.budget.id,
                    operation.userId,
                    operation.value
                )
                viewModelScope.launch {
                    api.postBudgetOperation(
                        auth,
                        operationDTO.budgetId,
                        operationDTO
                    )

                    _budgetState.value = budgetState.value.copy(isLoading = true)

                    val budget = budgetState.value.budget

                    budget?.let {
                        val operationsDTO: List<BudgetOperationDTO> = api.getBudgetOperations(
                            auth,
                            operationDTO.budgetId
                        )

                        val operations: List<Operation> = operationsDTO.map { operation ->
                            Operation(
                                operation.id,
                                operation.name,
                                budget,
                                operation.userId,
                                operation.value
                            )
                        }
                        budget.operations = operations
                    }

                    _budgetState.value = budgetState.value.copy(
                        budget = budget,
                        isLoading = false
                    )

                }
            }
        } catch (e: Exception) {
            Log.e("BudgetDetailsScreenViewModel", "createOperation: ", e)
        }
    }

    data class BudgetState(
        val budget: Budget? = null,
        val isLoading: Boolean = false
    )

}