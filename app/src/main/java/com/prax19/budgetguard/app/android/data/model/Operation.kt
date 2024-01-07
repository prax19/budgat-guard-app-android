package com.prax19.budgetguard.app.android.data.model

data class Operation(
    val id: Long,
    val name: String,
    val budget: Budget,
    val userId: Long,
    val operationValue: Float
)