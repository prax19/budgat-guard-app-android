package com.prax19.budgetguard.app.android.util

import com.prax19.budgetguard.app.android.data.auth.AuthResult

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val authResult: AuthResult<T>? = null) {

    class Success<T>(
        data: T
    ) : Resource<T>(data = data)

    class Error<T>(
        message: String? = null,
        authResult: AuthResult<T>? = null
    ) : Resource<T>(message = message, authResult = authResult)

    class  Loading<T>(
        data: T? = null
    ) : Resource<T>(data = data)

}