package com.prax19.budgetguard.app.android.presentation.MainScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddEditBudgetDialog(
    onSave: (name: String) -> Unit,
    onDismissRequest: () -> Unit
) {

    val saveButtonFocusRequester = remember { FocusRequester() }

    var name by remember { mutableStateOf("") }
    val nameFocusRequester = remember { FocusRequester() }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Add new budget")
                        },
                        actions = {
                            TextButton(
                                modifier = Modifier
                                    .focusRequester(saveButtonFocusRequester),
                                onClick = {
                                    onSave(name)
                                }
                            ) {
                                Text("save")
                            }
                        }
                    )
                },
                content = {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = it.calculateBottomPadding())
                            .padding(top = it.calculateTopPadding())
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {

                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(nameFocusRequester)
                                    .padding(4.dp),
                                value = name,
                                onValueChange = {
                                    name = it
                                },
                                placeholder = {
                                    Text(text = "Your budget name")
                                },
                                singleLine = true,
                                label = {
                                    Text(text = "Name")
                                },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onAny = {
                                        saveButtonFocusRequester.requestFocus()
                                    }
                                )
                            )
                        }
                    }
                }
            )
        }
    }

}