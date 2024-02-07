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

    data object Week: OperationsFilter("This week") {
        override fun doFilter(operations: List<Operation>): List<Operation> {
            val today = LocalDate.now()
            return operations.filter {
                it.dateTime.toLocalDate().isAfter(today.minusWeeks(1))
                    && it.dateTime.toLocalDate().isBefore(today.plusDays(1))
            }
        }
    }

    data object Month: OperationsFilter("This month") {
        override fun doFilter(operations: List<Operation>): List<Operation> {
            val today = LocalDate.now()
            return operations.filter {
                it.dateTime.toLocalDate().isAfter(today.minusMonths(1))
                        && it.dateTime.toLocalDate().isBefore(today.plusDays(1))
            }
        }
    }

}