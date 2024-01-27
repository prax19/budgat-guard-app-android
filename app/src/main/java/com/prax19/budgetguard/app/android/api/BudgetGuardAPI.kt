package com.prax19.budgetguard.app.android.api

import com.prax19.budgetguard.app.android.data.auth.Credentials
import com.prax19.budgetguard.app.android.data.auth.Token
import com.prax19.budgetguard.app.android.data.dto.BudgetDTO
import com.prax19.budgetguard.app.android.data.dto.BudgetOperationDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BudgetGuardApi {

    companion object {
        //const val BASE_URL = "http://192.168.1.205:8080/api/v1/"
        const val BASE_URL = "https://osprey-brief-silkworm.ngrok-free.app/api/v1/"
    }

    @POST("auth/register")
    suspend fun signUp(
        @Body credentials: Credentials.SignUp
    ): Token

    @POST("auth/authenticate")
    suspend fun signIn(
        @Body credentials: Credentials.SignIn
    ): Token

    @POST("budget")
    suspend fun postBudget(
        @Body budgetDTO: BudgetDTO
    )

    @PUT("budget/{id}")
    suspend fun putBudget(
        @Path("id") id: Long,
        @Body budgetDTO: BudgetDTO
    )

    @GET("budget/{id}")
    suspend fun getBudget(
        @Path("id") id: Long
    ): BudgetDTO

    @GET("budget")
    suspend fun getAllBudgets(): List<BudgetDTO>

    @DELETE("budget/{id}")
    suspend fun deleteBudget(
        @Path("id") id: Long
    )

    @GET("budget/{id}/operations")
    suspend fun getBudgetOperations(
        @Path("id") id: Long
    ): List<BudgetOperationDTO>

    @GET("budget/{budgetId}/operation/{id}")
    suspend fun getBudgetOperation(
        @Path("budgetId") budgetId: Long,
        @Path("id") id: Long
    ): BudgetOperationDTO

    @POST("budget/{id}/operation")
    suspend fun postBudgetOperation(
        @Path("id") id: Long,
        @Body operationDTO: BudgetOperationDTO
    )

    @PUT("budget/{budgetId}/operation/{id}")
    suspend fun putOperation(
        @Path("budgetId") budgetId: Long,
        @Path("id") id: Long,
        @Body operationDTO: BudgetOperationDTO
    )

    @DELETE("budget/{budgetId}/operation/{id}")
    suspend fun deleteOperation(
        @Path("budgetId") budgetId: Long,
        @Path("id") id: Long
    )

}