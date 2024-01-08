package com.prax19.budgetguard.app.android.presentation.AddEditBudgetOperationScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
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
import androidx.compose.ui.Alignment
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
    onSave: (name: String, value: Float) -> Unit,
    onDismissRequest: () -> Unit
) {

    val saveButtonFocusRequester = remember { FocusRequester() }

    var name by remember { mutableStateOf("") }
    val nameFocusRequester = remember { FocusRequester() }

    val operationTypes = listOf("expense", "income")
    var selectedType by remember { mutableStateOf(operationTypes[0]) }

    var value by remember { mutableStateOf("") }
    val valueFocusRequester = remember { FocusRequester() }

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
                            Text(text = "Add new operation")
                        },
                        actions = {
                            TextButton(
                                modifier = Modifier
                                    .focusRequester(saveButtonFocusRequester),
                                onClick = {
                                    val valueFloat = if (selectedType == operationTypes[0])
                                        value.toFloat() * -1
                                    else
                                        value.toFloat()
                                    onSave(name, valueFloat)
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

                            Row {
                                operationTypes.forEach { operationType ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        InputChip(
                                            modifier = Modifier
                                                .padding(4.dp),
                                            selected = (operationType == selectedType),
                                            onClick = { selectedType = operationType },
                                            label = { Text(operationType) },
                                        )
                                    }
                                }
                            }

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
                                        val valueFloat = if (selectedType == operationTypes[0])
                                            value.toFloat() * -1
                                        else
                                            value.toFloat()
                                        onSave(name, valueFloat)
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