package com.prax19.budgetguard.app.android.data.model

data class User(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: String,
    val locked: Boolean,
    val enabled: Boolean
)