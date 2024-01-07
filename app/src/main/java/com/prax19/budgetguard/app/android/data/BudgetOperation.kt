package com.prax19.budgetguard.app.android.data

data class BudgetOperation(
    val id: Long,
    val name: String,
    val budgetId: Long,
    val userId: Long,
    val operationValue: Float
)