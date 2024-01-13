package com.prax19.budgetguard.app.android.presentation.sign_in

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
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    var login = mutableStateOf("")
    val loginInputFocusRequester = FocusRequester()
    val isLoginInputBlank = mutableStateOf(true)

    var password = mutableStateOf("")
    val passwordInputFocusRequester = FocusRequester()
    val isPasswordInputBlank = mutableStateOf(true)
    val isPasswordHidden = mutableStateOf(true)

    val loginButtonFocusRequester = FocusRequester()

    fun signIn() {
        viewModelScope.launch {
            try {
                authRepository.signIn(
                    Credentials.SignIn(
                        login.value,
                        password.value
                    )
                )
            } catch (e: Exception) {
                Log.e("EditUserDetailsViewModel", "signIn: ", e)
            }
        }
    }

}