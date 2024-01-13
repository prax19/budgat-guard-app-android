package com.prax19.budgetguard.app.android.presentation.sign_up

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prax19.budgetguard.app.android.data.auth.AuthResult
import com.prax19.budgetguard.app.android.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUpScreen(
    navController: NavController
) {

    val viewModel: SignUpViewModel = hiltViewModel()

    val loginInputFocusRequester = remember { viewModel.loginInputFocusRequester }
    val passwordInputFocusRequester = remember { viewModel.passwordInputFocusRequester }
    val nameInputFocusRequester = remember { viewModel.nameInputFocusRequester }
    val surnameInputFocusRequester = remember { viewModel.surnameInputFocusRequester }
    val saveButtonFocusRequester = remember { viewModel.saveButtonFocusRequester }

    val state = viewModel.state
    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.authResults.collect {
            when(it) {
                is AuthResult.Authorized -> {
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
                is AuthResult.Unauthorized -> {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                is AuthResult.UserNotFound -> {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                is AuthResult.Error -> {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

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
                value = state.login,
                onValueChange = {
                    viewModel.onEvent(SignUpUiEvent.UsernameChanged(it))
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
                enabled = state.isPasswordEnabled,
                singleLine = true,
                value = state.password,
                onValueChange = {
                    viewModel.onEvent(SignUpUiEvent.PasswordChanged(it))
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
                    if(state.isPasswordHidden)
                        PasswordVisualTransformation()
                    else
                        VisualTransformation.None,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(SignUpUiEvent.ChangePasswordVisibility)
                        },
                        enabled = state.isPasswordEnabled,
                        content = {
                            if(state.isPasswordHidden)
                                Icon(
                                    imageVector = Icons.Filled.VisibilityOff,
                                    contentDescription = "hide_show_password"
                                )
                            else
                                Icon(
                                    imageVector = Icons.Filled.Visibility,
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
                enabled = state.isNameEnabled,
                singleLine = true,
                value = state.name,
                onValueChange = {text ->
                    viewModel.onEvent(SignUpUiEvent.NameChanged(text))
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
                enabled = state.isSurnameEnabled,
                singleLine = true,
                value = state.surname,
                onValueChange = {
                    viewModel.onEvent(SignUpUiEvent.SurnameChanged(it))
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
                enabled = state.isFormComplete,
                onClick = {
                    viewModel.onEvent(SignUpUiEvent.SignUp)
                }
            )

        }
    }

}