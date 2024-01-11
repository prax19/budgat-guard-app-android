package com.prax19.budgetguard.app.android.presentation.sign_in

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(

): ViewModel() {

    var login = mutableStateOf("")
    val loginInputFocusRequester = FocusRequester()
    val isLoginInputBlank = mutableStateOf(true)

    var password = mutableStateOf("")
    val passwordInputFocusRequester = FocusRequester()
    val isPasswordInputBlank = mutableStateOf(true)
    val isPasswordHidden = mutableStateOf(true)

    val loginButtonFocusRequester = FocusRequester()

}