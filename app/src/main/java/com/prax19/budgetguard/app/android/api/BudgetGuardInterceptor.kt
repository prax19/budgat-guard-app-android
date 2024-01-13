package com.prax19.budgetguard.app.android.api

import android.content.SharedPreferences
import android.util.Log
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class BudgetGuardInterceptor @Inject constructor(
    private val preferences: SharedPreferences
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        if(isAuthEndpoint(request.url))
            return chain.proceed(request)

        val token = preferences.getString("jwt", null).toString()
        Log.e("TOKEN", token)
        val authenticatedRequest = chain.request().newBuilder().addHeader("Authorization", token).build()
        return chain.proceed(authenticatedRequest)
    }

    private fun isAuthEndpoint(url: HttpUrl): Boolean {

        val authEndpoints = listOf("auth/register", "auth/authenticate")
        return authEndpoints.any { url.encodedPath.endsWith(it) }
    }

}