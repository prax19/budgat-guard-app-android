package com.prax19.budgetguard.app.android.presentation.sign_up

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prax19.budgetguard.app.android.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditUserDetailsScreen(
    navController: NavController
) {

    val viewModel: EditUserDetailsViewModel = hiltViewModel()

    var login by remember { viewModel.login }
    val loginInputFocusRequester = remember { viewModel.loginInputFocusRequester }
    val isLoginInputBlank = remember { viewModel.isLoginInputBlank }

    var password by remember { viewModel.password }
    val passwordInputFocusRequester = remember { viewModel.passwordInputFocusRequester }
    val isPasswordInputBlank = remember { viewModel.isPasswordInputBlank }
    val isPasswordHidden = remember { viewModel.isPasswordHidden }

    var name by remember { viewModel.name }
    val nameInputFocusRequester = remember { viewModel.nameInputFocusRequester }
    val isNameInputBlank = remember { viewModel.isNameInputBlank }

    var surname by remember { viewModel.surname }
    val surnameInputFocusRequester = remember { viewModel.surnameInputFocusRequester }
    val isSurnameInputBlank = remember { viewModel.isSurnameInputBlank }

    val saveButtonFocusRequester = remember { viewModel.saveButtonFocusRequester }

    Scaffold(

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .imePadding()
                .wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(loginInputFocusRequester),
                label = {
                    Text(text = "Login")
                },
                singleLine = true,
                value = login,
                onValueChange = {text ->
                    login = text
                    isLoginInputBlank.value = text.isBlank()
                    if(text.isBlank()) password = ""
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.None
                ),
                keyboardActions = KeyboardActions(
                    onAny = {
                        passwordInputFocusRequester.requestFocus()
                    }
                )
            )

            Spacer(modifier = Modifier
                .size(8.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordInputFocusRequester),
                label = {
                    Text(text = "Password")
                },
                enabled = !isLoginInputBlank.value,
                singleLine = true,
                value = password,
                onValueChange = {text ->
                    password = text
                    isPasswordInputBlank.value = text.isBlank()
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(
                    onAny = {
                        nameInputFocusRequester.requestFocus()
                    }
                ),
                visualTransformation =
                if(isPasswordHidden.value)
                    PasswordVisualTransformation()
                else
                    VisualTransformation.None,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            isPasswordHidden.value = !isPasswordHidden.value
                        },
                        enabled = !isLoginInputBlank.value,
                        content = {
                            if(isPasswordHidden.value)
                                Icon(
                                    imageVector = Icons.Filled.Visibility,
                                    contentDescription = "hide_show_password"
                                )
                            else
                                Icon(
                                    imageVector = Icons.Filled.VisibilityOff,
                                    contentDescription = "hide_show_password"
                                )
                        }
                    )
                }
            )

            Spacer(modifier = Modifier
                .size(8.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(nameInputFocusRequester),
                label = {
                        Text(text = "Name")
                },
                enabled = !isPasswordInputBlank.value,
                singleLine = true,
                value = name,
                onValueChange = {text ->
                    name = text
                    isNameInputBlank.value = text.isBlank()
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.None
                ),
                keyboardActions = KeyboardActions(
                    onAny = {
                        surnameInputFocusRequester.requestFocus()
                    }
                )
            )

            Spacer(modifier = Modifier
                .size(8.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(surnameInputFocusRequester),
                label = {
                    Text(text = "Surname")
                },
                enabled = !isPasswordInputBlank.value,
                singleLine = true,
                value = surname,
                onValueChange = {text ->
                    surname = text
                    isSurnameInputBlank.value = text.isBlank()
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.None
                ),
                keyboardActions = KeyboardActions(
                    onAny = {
                        saveButtonFocusRequester.requestFocus()
                    }
                )
            )

            Spacer(modifier = Modifier
                .size(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(saveButtonFocusRequester),
                content = {
                          Text("Save")
                },
                enabled =
                    !isLoginInputBlank.value &&
                    !isPasswordInputBlank.value &&
                    !isNameInputBlank.value &&
                    !isSurnameInputBlank.value,
                onClick = {
                    viewModel.registerUser()
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )

        }
    }

}