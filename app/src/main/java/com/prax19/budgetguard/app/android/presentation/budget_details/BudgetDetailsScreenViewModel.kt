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
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = mutableStateOf(BudgetState())
    val state: State<BudgetState> = _state

    private val resultChanel = Channel<AuthResult<*>>()
    val results = resultChanel.receiveAsFlow()

    val operationFilters = listOf(OperationsFilter.All, OperationsFilter.Today, OperationsFilter.Week)

    private val budgetId: Long? = null

    fun loadBudget() {
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
            _state.value = state.value.copy(isLoading = true)

            val result = budgetRepository.getBudget(budgetId)

            var budget: Budget
            result.data?.let {
                budget = Budget(
                    it.id,
                    it.name,
                    it.ownerId,
                    emptyList(),
                    it.balance
                )
                operationsRepository.getAllOperations(budget).data?.let { operations ->
                    budget = budget.copy(
                        operations = operations
                    )
                }

                _state.value = state.value.copy(
                    budget = budget,
                    opsToDisplay = state.value.filter.doFilter(budget.operations)
                )
            }
            result.authResult?.let {
                resultChanel.send(it)
            }
            _state.value = state.value.copy(
                isLoading = false,
                isViewReady = true
            )
        }
    }

    private fun refreshBudget() {
        state.value.budget?.let {
            loadBudget(it.id)
        }
    }

    fun getOperationById(id: Long?): Operation? {
        id?.let {
            if(id < 0)
                return null
            val operations = state.value.budget?.operations
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
            state.value.budget?.let {
                val result = operationsRepository.postOperation(
                    it, operation
                )
                suspend { refreshBudget() }.invoke()
                result.authResult?.let {
                    resultChanel.send(it)
                }
            }
        }
    }

    fun editOperation(operation: Operation) {
        viewModelScope.launch {
            state.value.budget?.let {
                val result = operationsRepository.putOperations(
                    it, operation
                )
                suspend { refreshBudget() }.invoke()
                result.authResult?.let {
                    resultChanel.send(it)
                }
            }
        }
    }

    fun deleteOperation(operation: Operation) {
        viewModelScope.launch {
            state.value.budget?.let {
                val result = operationsRepository.deleteOperation(
                    it, operation
                )
                suspend { refreshBudget() }.invoke()
                result.authResult?.let {
                    resultChanel.send(it)
                }
            }
        }

    }

    fun setFilter(filter: OperationsFilter) {
        _state.value = state.value.copy(filter = filter)
    }

    fun markViewAsNotReady() {
        _state.value = state.value.copy(isViewReady = false)
    }

    data class BudgetState(
        val budget: Budget? = null,
        val opsToDisplay: List<Operation> = emptyList(), //Operations displayed in a view
        val filter: OperationsFilter = OperationsFilter.All,
        val isLoading: Boolean = false,
        val isViewReady: Boolean = false
    )

}