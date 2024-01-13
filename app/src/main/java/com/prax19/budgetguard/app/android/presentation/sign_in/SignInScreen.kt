package com.prax19.budgetguard.app.android.presentation.sign_in

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
import androidx.compose.material3.OutlinedButton
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
fun SignInScreen(
    navController: NavController
) {

    val viewModel: SignInViewModel = hiltViewModel()

    val loginInputFocusRequester = remember { viewModel.loginInputFocusRequester }
    val passwordInputFocusRequester = remember { viewModel.passwordInputFocusRequester }
    val loginButtonFocusRequester = remember { viewModel.loginButtonFocusRequester }

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
                onValueChange = {text ->
                    viewModel.onEvent(SignInUiEvent.UsernameChanged(text))
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
                onValueChange = {text ->
                    viewModel.onEvent(SignInUiEvent.PasswordChanged(text))
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(
                    onAny = {
                        loginButtonFocusRequester.requestFocus()
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
                            viewModel.onEvent(SignInUiEvent.ChangePasswordVisibility)
                        },
                        enabled = state.isPasswordEnabled,
                        content = {
                            if(state.isPasswordHidden)
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
                .size(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(loginButtonFocusRequester)
                ,
                content = {
                      Text("Sign in")
                },
                enabled = state.isFormComplete,
                onClick = {
                    viewModel.onEvent(SignInUiEvent.SignIn)
                }
            )
            Spacer(modifier = Modifier
                .size(4.dp))
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(),
                content = {
                    Text("Sign up")
                },
                onClick = {
                    navController.navigate(Screen.EditUserDetailsScreen.route)
                }
            )

        }
    }

}