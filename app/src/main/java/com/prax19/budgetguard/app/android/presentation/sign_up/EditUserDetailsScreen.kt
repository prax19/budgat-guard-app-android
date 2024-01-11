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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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

    var name by remember { mutableStateOf("") }
    val nameInputFocusRequester = remember { FocusRequester() }

    var surname by remember { mutableStateOf("") }
    val surnameInputFocusRequester = remember { FocusRequester() }

    val saveButtonFocusRequester = remember { FocusRequester() }

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
                    .focusRequester(nameInputFocusRequester),
                label = {
                        Text(text = "Name")
                },
                singleLine = true,
                value = name,
                onValueChange = {text ->
                    name = text
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

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(surnameInputFocusRequester),
                label = {
                    Text(text = "Surname")
                },
                singleLine = true,
                value = surname,
                onValueChange = {text ->
                    surname = text
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
                .size(8.dp))



            Spacer(modifier = Modifier
                .size(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(saveButtonFocusRequester)
                ,
                content = {
                          Text("Save")
                },
                onClick = {
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