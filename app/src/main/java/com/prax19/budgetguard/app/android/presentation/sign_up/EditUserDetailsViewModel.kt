package com.prax19.budgetguard.app.android.presentation.sign_up

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prax19.budgetguard.app.android.data.model.User
import com.prax19.budgetguard.app.android.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditUserDetailsViewModel @Inject constructor(
    private val userRepository: UserRepository
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

    fun registerUser() {
        viewModelScope.launch {
            try {
                userRepository.registerUser(
                    User(-1,
                        name.value,
                        surname.value,
                        login.value,
                        "USER",
                        locked = false,
                        enabled = true
                    ),
                    password.value
                )
            } catch (e: Exception) {
                Log.e("EditUserDetailsViewModel", "registerUser: ", e)
            }
        }
    }

}