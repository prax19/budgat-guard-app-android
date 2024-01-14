package com.prax19.budgetguard.app.android.data.auth

sealed class AuthResult<T>(val data: T? = null, val message: String? = null) {

    class Authorized<T>(
        data: T? = null
    ): AuthResult<T>(data)

    class Unauthorized<T>(
        message: String? = "User not authorized!"
    ): AuthResult<T>(message = message)

    class UserNotFound<T>(
        message: String = "User not found!"
    ): AuthResult<T>(message = message)

    class Error<T>(
        message: String = "Unknown error!"
    ): AuthResult<T>(message = message)

}