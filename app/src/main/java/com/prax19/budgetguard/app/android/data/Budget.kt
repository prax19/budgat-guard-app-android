package com.prax19.budgetguard.app.android.data

data class Budget(
    val id: Long,
    val name: String,
    val ownerId: Long,
    val operations: List<Long>
)