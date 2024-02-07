package com.prax19.budgetguard.app.android.data.model

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Target(
    val startDate: LocalDate,
    val startValue: Float,
    val endDate: LocalDate,
    val endValue: Float
) {

    fun getAdvice(value: Float, date: LocalDate): Float? {
        if(date in startDate..endDate) {
            val daysUntilEnd = ChronoUnit.DAYS.between(date, endDate)
            return endValue.minus(value).div(daysUntilEnd)
        }
        return null
    }

}