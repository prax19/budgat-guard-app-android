package com.prax19.budgetguard.app.android.presentation.BudgetScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prax19.budgetguard.app.android.api.BudgetGuardApi
import com.prax19.budgetguard.app.android.data.Budget
import com.prax19.budgetguard.app.android.data.BudgetOperation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetDetailsScreenViewModel @Inject constructor(
    private val api: BudgetGuardApi,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _budgetState = mutableStateOf(BudgetState())
    val budgetState: State<BudgetState> = _budgetState

//    private val _operationsState = mutableStateOf(OperationsState())
//    val operationsState: State<OperationsState> = _operationsState

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
                _budgetState.value = budgetState.value.copy(
                    budget = api.getBudget(
                        "Basic cGF0cnlrLnBpcm9nQG8zNjUudXMuZWR1LnBsOnBhc3N3b3Jk",
                        budgetId
                    ),
                    isLoading = false
                )
            } catch (e: Exception) {
                Log.e("BudgetDetailsScreenViewModel", "getBudget: ", e)
                _budgetState.value = budgetState.value.copy(isLoading = false)
            }
        }
    }

//    fun getOperations(budgetId: Long) {
//            try {
//                _operationsState.value = operationsState.value.copy(isLoading = true)
//                _operationsState.value = operationsState.value.copy(
//                    operations = api.getBudgetOperations(
//                        "Basic cGF0cnlrLnBpcm9nQG8zNjUudXMuZWR1LnBsOnBhc3N3b3Jk",
//                        budgetId
//                    ),
//                    isLoading = false
//                )
//            } catch (e: Exception) {
//                Log.e("BudgetDetailsScreenViewModel", "getBudgetOperations: ", e)
//                _operationsState.value = operationsState.value.copy(isLoading = false)
//            }
//    }

    data class BudgetState(
        val budget: Budget? = null,
        val isLoading: Boolean = false
    )

//    data class OperationsState(
//        val operations: List<BudgetOperation>? = null,
//        val isLoading: Boolean = false
//    )

}