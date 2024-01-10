package com.prax19.budgetguard.app.android.presentation.MainScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prax19.budgetguard.app.android.data.dto.BudgetDTO
import com.prax19.budgetguard.app.android.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: BudgetRepository
) : ViewModel() {

    private val auth = "Basic cGF0cnlrLnBpcm9nQG8zNjUudXMuZWR1LnBsOnBhc3N3b3Jk"

    private val _state = mutableStateOf(ListOfBudgetsState())
    val state: State<ListOfBudgetsState> = _state

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

    fun getBudgetById(id: Long?): BudgetDTO? {
        try {
            id?.let{
                if(id < 0)
                    return null
                val budgets = state.value.budgets
                budgets?.let {
                    for(budget: BudgetDTO in it) {
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

    fun createNewBudget(budgetDTO: BudgetDTO) {
        viewModelScope.launch {
            try {
                repository.postBudget(budgetDTO)
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "createNewBudget: ", e)
            }
            suspend { loadBudgets() }.invoke()
        }
    }

    fun editBudget(budgetDTO: BudgetDTO) {
        viewModelScope.launch {
            try {
                repository.putBudget(budgetDTO)
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "editExistingBudget: ", e)
            }
            suspend { loadBudgets() }.invoke()
        }
    }

    fun deleteBudget(budgetDTO: BudgetDTO) {
        viewModelScope.launch {
            try {
                repository.deleteBudget(budgetDTO)
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "deleteBudget: ", e)
            }
            suspend { loadBudgets() }.invoke()
        }
    }

    data class ListOfBudgetsState(
        val budgets: List<BudgetDTO> ?= emptyList(),
        val isLoading: Boolean = false
    )

}