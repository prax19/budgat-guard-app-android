package com.prax19.budgetguard.app.android.data.model

import java.time.LocalDateTime

data class Operation(
    val id: Long,
    val name: String,
    val budget: Budget,
    val dateTime: LocalDateTime,
    val userId: Long,
    var value: Float
)