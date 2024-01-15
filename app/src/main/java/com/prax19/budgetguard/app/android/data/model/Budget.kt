package com.prax19.budgetguard.app.android.data.model

data class Budget (
    val id: Long,
    val name: String,
    val ownerId: Long,
    var operations: List<Operation>,
    val balance: Float? = 0f
)