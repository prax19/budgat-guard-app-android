package com.prax19.budgetguard.app.android.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.prax19.budgetguard.app.android.api.BudgetGuardApi
import com.prax19.budgetguard.app.android.data.auth.AuthResult
import com.prax19.budgetguard.app.android.data.auth.Credentials
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.HttpException
import java.net.ConnectException
import javax.inject.Inject

@ActivityScoped
class AuthRepository @Inject constructor(
    private val api: BudgetGuardApi,
    private val preferences: SharedPreferences
): ViewModel() {

    suspend fun signUp(credentials: Credentials.SignUp): AuthResult<Unit> {
        return try {
            val token = api.signUp(credentials)
            preferences.edit().putString("jwt", "Bearer ${token.token}").apply()
            AuthResult.Authorized()
        } catch (e: HttpException) {
            Log.e("AuthRepository", "HTTP code: ${e.code()}")
            if(e.code() == 409)
                AuthResult.Error("Login already taken!")
            else
                AuthResult.Error()
        } catch (e: ConnectException) {
            Log.e("AuthRepository", "Cannot connect to the server!")
            AuthResult.Error("Cannot connect to the server!")
        } catch (e: Exception) {
            Log.e("AuthRepository", "signUp: ${e.message}")
            AuthResult.Error()
        }
    }

    suspend fun signIn(credentials: Credentials.SignIn): AuthResult<Unit> {
        return try {
            val token = api.signIn(credentials)
            preferences.edit().putString("jwt", "Bearer ${token.token}").apply()
            AuthResult.Authorized()
        } catch (e: HttpException) {
            Log.e("AuthRepository", "HTTP code: ${e.code()}")
            if (e.code() == 401)
                AuthResult.Unauthorized()
            else if (e.code() == 403)
                AuthResult.Forbidden("Credentials incorrect!")
            else if (e.code() == 404)
                AuthResult.UserNotFound()
            else if (e.code() == 500)
                AuthResult.Error("Internal server error")
            else
                AuthResult.Error()
        } catch (e: ConnectException) {
            Log.e("AuthRepository", "Cannot connect to the server!")
            AuthResult.Error("Cannot connect to the server!")
        } catch (e: Exception) {
            Log.e("AuthRepository", "signUp: ${e.message}")
            AuthResult.Error()
        }
    }

}