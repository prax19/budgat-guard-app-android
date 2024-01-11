package com.prax19.budgetguard.app.android.presentation.sign_in

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
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
fun SignInScreen(
    navController: NavController
) {

    val viewModel: SignInViewModel = hiltViewModel()

    var login by remember { mutableStateOf("") }
    val loginInputFocusRequester = remember { FocusRequester() }
    val isLoginInputBlank = remember { mutableStateOf(true) }

    var password by remember { mutableStateOf("") }
    val passwordInputFocusRequester = remember { FocusRequester() }
    val isPasswordInputBlank = remember { mutableStateOf(true) }
    val isPasswordHidden = remember { mutableStateOf(true) }

    val loginButtonFocusRequester = remember { FocusRequester() }

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
                        loginButtonFocusRequester.requestFocus()
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
                .size(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(loginButtonFocusRequester)
                ,
                content = {
                          Text("Sign in")
                },
                enabled = !isLoginInputBlank.value && !isPasswordInputBlank.value,
                onClick = {
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
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