package com.prax19.budgetguard.app.android.presentation.sign_up

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prax19.budgetguard.app.android.data.auth.Credentials
import com.prax19.budgetguard.app.android.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditUserDetailsViewModel @Inject constructor(
    private val authRepository: AuthRepository
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

    fun signUp() {
        viewModelScope.launch {
            try {
                authRepository.signUp(
                    Credentials.SignUp(
                        login.value,
                        password.value,
                        name.value,
                        surname.value
                    )
                )
            } catch (e: Exception) {
                Log.e("EditUserDetailsViewModel", "signUp: ", e)
            }
        }
    }

}