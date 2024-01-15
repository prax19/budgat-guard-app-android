package com.prax19.budgetguard.app.android.data.dto

data class BudgetDTO(
    val id: Long,
    val name: String,
    val ownerId: Long,
    val operations: List<Long>,
    val balance: Float? = 0f
)