package com.prax19.budgetguard.app.android.presentation.sign_up

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prax19.budgetguard.app.android.data.auth.AuthResult
import com.prax19.budgetguard.app.android.data.auth.Credentials
import com.prax19.budgetguard.app.android.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    var state by mutableStateOf(SignUpState())

    private val resultChanel = Channel<AuthResult<Unit>>()
    val authResults = resultChanel.receiveAsFlow()

    var loginInputFocusRequester = FocusRequester()
    var passwordInputFocusRequester = FocusRequester()
    var nameInputFocusRequester = FocusRequester()
    var surnameInputFocusRequester = FocusRequester()
    var saveButtonFocusRequester = FocusRequester()

    fun onEvent(event: SignUpUiEvent) {
        when(event) {
            is SignUpUiEvent.UsernameChanged -> {
                state = state.copy(login = event.value)
                if(state.login.isBlank()) {
                    state = state.copy(
                        isPasswordEnabled = false
                    )
                } else {
                    state = state.copy(
                        isPasswordEnabled = true
                    )
                }
            }
            is SignUpUiEvent.PasswordChanged -> {
                state = state.copy(password = event.value)
                if(state.password.isBlank()) {
                    state = state.copy(
                        isNameEnabled = false,
                        isSurnameEnabled = false
                    )
                } else {
                    state = state.copy(
                        isNameEnabled = true,
                        isSurnameEnabled = true
                    )
                }
            }
            is SignUpUiEvent.NameChanged -> {
                state = state.copy(name = event.value)
                if(state.name.isBlank() || state.surname.isBlank())
                    state = state.copy(isFormComplete = false)
                else
                    state = state.copy(isFormComplete = true)
            }
            is SignUpUiEvent.SurnameChanged -> {
                state = state.copy(surname = event.value)
                if(state.name.isBlank() || state.surname.isBlank())
                    state = state.copy(isFormComplete = false)
                else
                    state = state.copy(isFormComplete = true)
            }
            is SignUpUiEvent.ChangePasswordVisibility -> {
                state = state.copy(isPasswordHidden = !state.isPasswordHidden)
            }
            is SignUpUiEvent.SignUp -> {

            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = authRepository.signUp(
                Credentials.SignUp(
                    state.login,
                    state.password,
                    state.name,
                    state.surname
                )
            )
            resultChanel.send(result)
            state = state.copy(isLoading = false)
        }
    }

    data class SignUpState(
        val isLoading: Boolean = false,
        val login: String = "",
        val password: String = "",
        val isPasswordEnabled: Boolean = false,
        val isPasswordHidden: Boolean = true,
        val name: String = "",
        val isNameEnabled: Boolean = false,
        val surname: String = "",
        val isSurnameEnabled: Boolean = false,
        val isFormComplete: Boolean = false
    )

}