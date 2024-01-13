package com.prax19.budgetguard.app.android.data.auth

sealed class Credentials(
    val login: String?,
    val password: String?,
    val firstName: String? = null,
    val lastName: String? = null
) {

    class SignUp(
        login: String,
        password: String,
        firstName: String,
        lastName: String
    ): Credentials(login, password, firstName, lastName)

    class SignIn(
        login: String,
        password: String
    ): Credentials(login, password)

}