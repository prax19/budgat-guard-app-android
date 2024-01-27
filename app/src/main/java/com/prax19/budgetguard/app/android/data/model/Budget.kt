package com.prax19.budgetguard.app.android.data.model

import java.util.Currency

data class Budget (
    val id: Long,
    val name: String,
    val ownerId: Long,
    var operations: List<Operation>,
    val balance: Float? = 0f,
    val currency: Currency = Currency.getInstance("PLN") //TODO: handle currencies in the backend
)