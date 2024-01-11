package com.prax19.budgetguard.app.android.presentation.sign_up

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditUserDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var login = mutableStateOf("")
    var loginInputFocusRequester = FocusRequester()
    var isLoginInputBlank = mutableStateOf(true)

    var password = mutableStateOf("")
    var passwordInputFocusRequester = FocusRequester()
    var isPasswordInputBlank = mutableStateOf(true)
    var isPasswordHidden = mutableStateOf(true)

    var name = mutableStateOf("")
    var nameInputFocusRequester = FocusRequester()
    var isNameInputBlank = mutableStateOf(true)

    var surname = mutableStateOf("")
    var surnameInputFocusRequester = FocusRequester()
    var isSurnameInputBlank = mutableStateOf(true)

    var saveButtonFocusRequester = FocusRequester()

}