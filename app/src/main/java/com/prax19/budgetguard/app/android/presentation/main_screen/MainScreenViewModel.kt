package com.prax19.budgetguard.app.android.presentation.main_screen

import android.util.Log
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
            try {
                _state.value = state.value.copy(isLoading = true)
                _state.value = state.value.copy(
                    budgets = repository.getAllBudgets().data,
                    isLoading = false
                )
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "getAllBudgets: ", e)
                _state.value = state.value.copy(isLoading = false)
            }
        }
    }

    private fun refreshBudgets() {
        loadBudgets()
    }

    //TODO: replace this with repository function
    fun getBudgetById(id: Long?): Budget? {
        try {
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
        } catch (e: Exception) {
            Log.e("MainScreenViewModel", "getBudget: ", e)
            return null
        }
    }

    fun createNewBudget(budget: Budget) {
        viewModelScope.launch {
            try {
                repository.postBudget(budget)
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "createNewBudget: ", e)
            }
            suspend { refreshBudgets() }.invoke()
        }
    }

    fun editBudget(budget: Budget) {
        viewModelScope.launch {
            try {
                repository.putBudget(budget)
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "editExistingBudget: ", e)
            }
            suspend { loadBudgets() }.invoke()
        }
    }

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            try {
                repository.deleteBudget(budget)
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "deleteBudget: ", e)
            }
            suspend { refreshBudgets() }.invoke()
        }
    }

    data class ListOfBudgetsState(
        val budgets: List<Budget> ?= emptyList(),
        val isLoading: Boolean = false
    )

}