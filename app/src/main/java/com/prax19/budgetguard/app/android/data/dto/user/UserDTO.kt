package com.prax19.budgetguard.app.android.data.dto.user

data class UserDTO(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val role: String,
    val locked: Boolean,
    val enabled: Boolean
)