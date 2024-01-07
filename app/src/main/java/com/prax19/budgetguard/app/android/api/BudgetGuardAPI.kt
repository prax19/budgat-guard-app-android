package com.prax19.budgetguard.app.android.api

import com.prax19.budgetguard.app.android.data.Budget
import retrofit2.http.GET
import retrofit2.http.Header

interface BudgetGuardApi {

    companion object {
        const val BASE_URL = "http://192.168.0.205:8080/api/v1/"
    }

//    @GET("budget/2")
//    suspend fun getBudget(@Header("Authorization") auth: String): Budget

    @GET("budget")
    suspend fun getAllBudgets(@Header("Authorization") auth: String): List<Budget>
}