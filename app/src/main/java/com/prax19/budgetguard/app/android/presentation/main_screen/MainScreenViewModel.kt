package com.prax19.budgetguard.app.android.presentation.main_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prax19.budgetguard.app.android.data.auth.AuthResult
import com.prax19.budgetguard.app.android.data.model.Budget
import com.prax19.budgetguard.app.android.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: BudgetRepository
) : ViewModel() {

    private val _state = mutableStateOf(ListOfBudgetsState())
    val state: State<ListOfBudgetsState> = _state

    private val authErrorChanel = Channel<AuthResult<Unit>>()
    val authErrors = authErrorChanel.receiveAsFlow()

    init {
        loadBudgets()
    }

    fun loadBudgets() {
        viewModelScope.launch {
                _state.value = state.value.copy(isLoading = true)
                _state.value = state.value.copy(
                    budgets = repository.getAllBudgets().data,
                    isLoading = false
                )
        }
    }

    private fun refreshBudgets() {
        loadBudgets()
    }

    //TODO: replace this with repository function
    fun getBudgetById(id: Long?): Budget? {
            id?.let{
                if(id < 0)
                    return null
                val budgets = state.value.budgets
                budgets?.let {
                    for(budget: Budget in it) {
                        if(budget.id == id)
                            return budget
                    }
                }
            }
            return null
    }

    fun createNewBudget(budget: Budget) {
        viewModelScope.launch {
            repository.postBudget(budget)
            suspend { refreshBudgets() }.invoke()
        }
    }

    fun editBudget(budget: Budget) {
        viewModelScope.launch {
            repository.putBudget(budget)
            suspend { loadBudgets() }.invoke()
        }
    }

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            repository.deleteBudget(budget)
            suspend { refreshBudgets() }.invoke()
        }
    }

    data class ListOfBudgetsState(
        val budgets: List<Budget> ?= emptyList(),
        val isLoading: Boolean = false
    )

}