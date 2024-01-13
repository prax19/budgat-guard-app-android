package com.prax19.budgetguard.app.android.presentation.sign_up

sealed class SignUpUiEvent {

    data class UsernameChanged(val value: String): SignUpUiEvent()
    data class PasswordChanged(val value: String): SignUpUiEvent()
    data class NameChanged(val value: String): SignUpUiEvent()
    data class SurnameChanged(val value: String): SignUpUiEvent()

    data object ChangePasswordVisibility: SignUpUiEvent()

    data object SignUp: SignUpUiEvent()

}