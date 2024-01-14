package com.prax19.budgetguard.app.android.presentation.budget_details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prax19.budgetguard.app.android.data.auth.AuthResult
import com.prax19.budgetguard.app.android.data.model.Budget
import com.prax19.budgetguard.app.android.data.model.Operation
import com.prax19.budgetguard.app.android.repository.BudgetRepository
import com.prax19.budgetguard.app.android.repository.OperationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetDetailsScreenViewModel @Inject constructor(
    private val operationsRepository: OperationRepository,
    private val budgetRepository: BudgetRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _budgetState = mutableStateOf(BudgetState())
    val budgetState: State<BudgetState> = _budgetState

    private val resultChanel = Channel<AuthResult<*>>()
    val results = resultChanel.receiveAsFlow()

    private val budgetId: Long? = null

    init {
        savedStateHandle.get<Long>("budgetId")?.let { budgetId ->
            if(budgetId != -1L) {
                viewModelScope.launch {
                    loadBudget(budgetId)
                }
            }
        }
    }

    fun loadBudget(budgetId: Long) {
        viewModelScope.launch {
            _budgetState.value = budgetState.value.copy(isLoading = true)

            val result = budgetRepository.getBudget(budgetId)

            var budget: Budget
            result.data?.let {
                budget = Budget(
                    it.id,
                    it.name,
                    it.ownerId,
                    emptyList()
                )
                operationsRepository.getAllOperations(budget).data?.let { operations ->
                    budget = budget.copy(
                        operations = operations
                    )
                }

                _budgetState.value = budgetState.value.copy(
                    budget = budget
                )
            }
            result.authResult?.let {
                resultChanel.send(it)
            }
            _budgetState.value = budgetState.value.copy(isLoading = false)
        }
    }

    private fun refreshBudget() {
        budgetState.value.budget?.let {
            loadBudget(it.id)
        }
    }

    fun getOperationById(id: Long?): Operation? {
        id?.let {
            if(id < 0)
                return null
            val operations = budgetState.value.budget?.operations
            operations?.let {
                for(operation: Operation in it) {
                    if(operation.id == id)
                        return operation
                }
            }
        }
        return null
    }

    fun createOperation(operation: Operation) {
        viewModelScope.launch {
            _budgetState.value = budgetState.value.copy(isLoading = true)
            budgetState.value.budget?.let {
                val result = operationsRepository.postOperation(
                    it, operation
                )
                suspend { refreshBudget() }.invoke() // TODO: test if it is necessary
                result.authResult?.let {
                    resultChanel.send(it)
                }
            }
            _budgetState.value = budgetState.value.copy(isLoading = false)
        }
    }

    fun editOperation(operation: Operation) {
        viewModelScope.launch {
            _budgetState.value = budgetState.value.copy(isLoading = true)
            budgetState.value.budget?.let {
                val result = operationsRepository.putOperations(
                    it, operation
                )
                suspend { refreshBudget() }.invoke() // TODO: test if it is necessary
                result.authResult?.let {
                    resultChanel.send(it)
                }
            }
            _budgetState.value = budgetState.value.copy(isLoading = false)
        }
    }

    fun deleteOperation(operation: Operation) {
        viewModelScope.launch {
            _budgetState.value = budgetState.value.copy(isLoading = true)
            budgetState.value.budget?.let {
                val result = operationsRepository.deleteOperation(
                    it, operation
                )
                suspend { refreshBudget() }.invoke()
                result.authResult?.let {
                    resultChanel.send(it)
                }
            }
            _budgetState.value = budgetState.value.copy(isLoading = false)
        }

    }

    data class BudgetState(
        val budget: Budget? = null,
        val isLoading: Boolean = false
    )

}