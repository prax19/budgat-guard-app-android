package com.prax19.budgetguard.app.android.presentation.sign_in

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var state by mutableStateOf(SignInState())

    val loginInputFocusRequester = FocusRequester()
    val passwordInputFocusRequester = FocusRequester()
    val loginButtonFocusRequester = FocusRequester()

    fun onEvent(event: SignInUiEvent) {
        when(event) {
            is SignInUiEvent.UsernameChanged -> {
                state = state.copy(login = event.value)
                if(state.login.isBlank()) {
                    state = state.copy(
                        isPasswordEnabled = false,
                        isFormComplete = false
                    )
                } else {
                    state = state.copy(
                        isPasswordEnabled = true,
                        isFormComplete =
                            state.password.isNotBlank()
                    )
                }
            }
            is SignInUiEvent.PasswordChanged -> {
                state = state.copy(password = event.value)
                if(state.password.isBlank()) {
                    state = state.copy(
                        isFormComplete = false,
                    )
                } else {
                    state = state.copy(
                        isFormComplete =
                            state.login.isNotBlank()
                    )
                }
            }
            is SignInUiEvent.ChangePasswordVisibility -> {
                state = state.copy(isPasswordHidden = !state.isPasswordHidden)
            }
            is SignInUiEvent.SignIn -> {

            }
        }
    }

    fun signIn() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            authRepository.signIn(
                Credentials.SignIn(
                    state.login,
                    state.password
                )
            )
            state = state.copy(isLoading = false)
        }
    }

    data class SignInState(
        val isLoading: Boolean = false,
        val login: String = "",
        val password: String = "",
        val isPasswordEnabled: Boolean = false,
        val isPasswordHidden: Boolean = true,
        val isFormComplete: Boolean = false
    )

}