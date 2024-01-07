package com.prax19.budgetguard.app.android.api

import com.prax19.budgetguard.app.android.data.Budget
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface BudgetGuardApi {

    companion object {
        const val BASE_URL = "http://192.168.0.205:8080/api/v1/"
    }

    @GET("budget/{id}")
    suspend fun getBudget(
        @Header("Authorization") auth: String,
        @Path("id") id: Long
    ): Budget

    @GET("budget")
    suspend fun getAllBudgets(
        @Header("Authorization") auth: String
    ): List<Budget>
}