package com.prax19.budgetguard.app.android.data.auth

sealed class AuthResult<T>(val data: T? = null, val message: String? = null) {

    class Authorized<T>(data: T? = null): AuthResult<T>(data)
    class Unauthorized<T>: AuthResult<T>()
    class Error<T>(message: String? = null): AuthResult<T>(message = message)

}