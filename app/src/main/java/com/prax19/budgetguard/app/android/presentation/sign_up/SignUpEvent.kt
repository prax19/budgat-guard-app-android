package com.prax19.budgetguard.app.android.presentation.sign_up

sealed class EditUserDetailsUiEvent {

    data class UsernameChanged(val value: String): EditUserDetailsUiEvent()
    data class PasswordChanged(val value: String): EditUserDetailsUiEvent()
    data class NameChanged(val value: String): EditUserDetailsUiEvent()
    data class SurnameChanged(val value: String): EditUserDetailsUiEvent()

    data object ChangePasswordVisibility: EditUserDetailsUiEvent()

    data object SignUp: EditUserDetailsUiEvent()

}