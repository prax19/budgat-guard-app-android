package com.prax19.budgetguard.app.android.presentation.budget_details

import com.prax19.budgetguard.app.android.data.model.Operation
import java.time.LocalDate

sealed class OperationsFilter(val text: String) {
    abstract fun doFilter(operations: List<Operation>): List<Operation>

    data object All: OperationsFilter("All") {
        override fun doFilter(operations: List<Operation>): List<Operation> {
            return operations
        }
    }
    data object Today: OperationsFilter("Today") {
        override fun doFilter(operations: List<Operation>): List<Operation> {
            return operations.filter {
                it.dateTime.toLocalDate().isEqual(LocalDate.now())
            }
        }
    }
}