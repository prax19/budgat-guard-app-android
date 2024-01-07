package com.prax19.budgetguard.app.android.data.dto

data class BudgetOperationDTO(
    val id: Long,
    val name: String,
    val budgetId: Long,
    val userId: Long,
    val value: Float
)