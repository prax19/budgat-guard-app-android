package com.prax19.budgetguard.app.android.data.model

data class Budget (
    val id: Long,
    val name: String,
    val ownerId: Long,
    val operations: List<Operation>
)