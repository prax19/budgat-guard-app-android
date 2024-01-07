package com.prax19.budgetguard.app.android.presentation.MainScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prax19.budgetguard.app.android.data.Budget
import com.prax19.budgetguard.app.android.api.BudgetGuardApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val api: BudgetGuardApi
) : ViewModel() {

    private val _state = mutableStateOf(ListOfBudgetsState())
    val state: State<ListOfBudgetsState> = _state

    init {
        getAllBudgets()
    }

    fun getAllBudgets() {
        viewModelScope.launch {
            try {
                _state.value = state.value.copy(isLoading = true)
                _state.value = state.value.copy(
                    budgets = api.getAllBudgets("Basic cGF0cnlrLnBpcm9nQG8zNjUudXMuZWR1LnBsOnBhc3N3b3Jk"),
                    isLoading = false
                )
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "getAllBudgets: ", e)
                _state.value = state.value.copy(isLoading = false)
            }
        }
    }

    data class ListOfBudgetsState(
        val budgets: List<Budget> ?= emptyList(),
        val isLoading: Boolean = false
    )

}