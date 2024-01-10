package com.prax19.budgetguard.app.android.api

import com.prax19.budgetguard.app.android.data.dto.BudgetDTO
import com.prax19.budgetguard.app.android.data.dto.BudgetOperationDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BudgetGuardApi {

    companion object {
        const val BASE_URL = "http://192.168.0.205:8080/api/v1/"
    }

    @POST("budget")
    suspend fun postBudget(
        @Header("Authorization") auth: String,
        @Body budgetDTO: BudgetDTO
    )

    @PUT("budget/{id}")
    suspend fun putBudget(
        @Header("Authorization") auth: String,
        @Path("id") id: Long,
        @Body budgetDTO: BudgetDTO
    )

    @GET("budget/{id}")
    suspend fun getBudget(
        @Header("Authorization") auth: String,
        @Path("id") id: Long
    ): BudgetDTO

    @GET("budget")
    suspend fun getAllBudgets(
        @Header("Authorization") auth: String
    ): List<BudgetDTO>

    @DELETE("budget/{id}")
    suspend fun deleteBudget(
        @Header("Authorization") auth: String,
        @Path("id") id: Long
    )

    @GET("budget/{id}/operations")
    suspend fun getBudgetOperations(
        @Header("Authorization") auth: String,
        @Path("id") id: Long
    ): List<BudgetOperationDTO>

    @POST("budget/{id}/operation")
    suspend fun postBudgetOperation(
        @Header("Authorization") auth: String,
        @Path("id") id: Long,
        @Body operationDTO: BudgetOperationDTO
    )

    @PUT("budget/{budgetId}/operation/{id}")
    suspend fun putOperation(
        @Header("Authorization") auth: String,
        @Path("budgetId") budgetId: Long,
        @Path("id") id: Long,
        @Body operationDTO: BudgetOperationDTO
    )

    @DELETE("budget/{budgetId}/operation/{id}")
    suspend fun deleteOperation(
        @Header("Authorization") auth: String,
        @Path("budgetId") budgetId: Long,
        @Path("id") id: Long
    )

}