package com.prax19.budgetguard.app.android.presentation.sign_in

sealed class SignInUiEvent {

    data class UsernameChanged(val value: String): SignInUiEvent()
    data class PasswordChanged(val value: String): SignInUiEvent()

    data object ChangePasswordVisibility: SignInUiEvent()
    data object SignIn: SignInUiEvent()

}