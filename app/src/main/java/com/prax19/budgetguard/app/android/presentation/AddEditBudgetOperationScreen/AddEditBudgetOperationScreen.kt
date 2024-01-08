package com.prax19.budgetguard.app.android.presentation.AddEditBudgetOperationScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddEditOperationScreen(
    onSave: (name: String, value: String) -> Unit,
    onDismissRequest: () -> Unit
) {

    val saveButtonFocusRequester = remember { FocusRequester() }

    var name by remember { mutableStateOf("") }
    val nameFocusRequester = remember { FocusRequester() }

    var value by remember { mutableStateOf("") }
    val valueFocusRequester = remember { FocusRequester() }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Add new operation")
                    },
                    actions = {
                        TextButton(
                            modifier = Modifier
                                .focusRequester(saveButtonFocusRequester),
                            onClick = { onSave(name, value) }
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
                            .padding(20.dp)
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
                                Text(text = "Your operation name")
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
                                    valueFocusRequester.requestFocus()
                                }
                            )
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(valueFocusRequester)
                                .padding(4.dp),
                            value = value,
                            onValueChange = {
                                value = it
                            },
                            placeholder = {
                                Text(text = "10.00")
                            },
                            singleLine = true,
                            label = {
                                Text(text = "Value")
                            },
                            suffix = {
                                Text(text = "zł")
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onAny = {
                                    onSave(name, value)
                                }
                            )
                        )
                    }
                }
            }
        )

    }

}