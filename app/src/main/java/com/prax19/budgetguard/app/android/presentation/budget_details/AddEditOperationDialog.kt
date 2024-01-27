package com.prax19.budgetguard.app.android.presentation.budget_details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
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
import com.prax19.budgetguard.app.android.data.dto.BudgetOperationDTO
import com.prax19.budgetguard.app.android.data.model.Operation
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

//TODO: prevent from entering empty data
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddEditOperationDialog(
    onOperationCreation: (operation: BudgetOperationDTO) -> Unit,
    onOperationEdition: (operation: BudgetOperationDTO) -> Unit,
    onDismissRequest: () -> Unit,
    operation: Operation?
) {

    val dialogName by remember {
        operation?.let {
            mutableStateOf("Edit operation")
        } ?: run {
            mutableStateOf("Create new operation")
        }
    }

    var defaultOperation: BudgetOperationDTO by remember {
        operation?.let {
            mutableStateOf(
                BudgetOperationDTO(
                    operation.id,
                    operation.name,
                    operation.budget.id,
                    operation.dateTime.toString(),
                    operation.userId,
                    operation.value
                )
            )
        } ?: run {
            mutableStateOf(
                BudgetOperationDTO(-1, "", -1, LocalDateTime.now().toString(), -1, 0f))
        }
    }

    val saveButtonFocusRequester = remember { FocusRequester() }

    var name by remember { mutableStateOf(defaultOperation.name) }
    val nameFocusRequester = remember { FocusRequester() }

    val operationTypes = listOf("expense", "income")
    var selectedType by remember {
        if(defaultOperation.value <= 0) {
            defaultOperation = defaultOperation.copy(
                value = defaultOperation.value * -1
            )
            mutableStateOf(operationTypes[0])
        } else
            mutableStateOf(operationTypes[1])
    }

    var openDatePicker by remember { mutableStateOf(false) }
    var openTimePicker by remember { mutableStateOf(false) }

    var dateTime by remember { mutableStateOf(LocalDateTime.parse(defaultOperation.dateTime)) }
    val timePickerState = rememberTimePickerState(dateTime.hour, dateTime.minute)
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dateTime.toEpochSecond(ZoneOffset.UTC) * 1000
    )

    var value by remember {
        if(defaultOperation.value == 0f)
            mutableStateOf("")
        else
            mutableStateOf(defaultOperation.value.toString())
    }
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
                            Text(text = dialogName)
                        },
                        actions = {
                            TextButton(
                                modifier = Modifier
                                    .focusRequester(saveButtonFocusRequester),
                                enabled = name.isNotBlank() && value.isNotBlank(),
                                onClick = {
                                    datePickerState.selectedDateMillis?.let {
                                        dateTime = LocalDateTime.ofInstant(
                                            Instant.ofEpochMilli(
                                                it
                                            ),
                                            ZoneOffset.UTC
                                        )
                                        dateTime = dateTime
                                            .plusHours(timePickerState.hour.toLong())
                                            .plusMinutes(timePickerState.minute.toLong())
                                    }
                                    val valueFloat = if (selectedType == operationTypes[0])
                                        value.toFloat() * -1
                                    else
                                        value.toFloat()
                                    val returnOperation =
                                        BudgetOperationDTO(
                                            defaultOperation.id,
                                            name,
                                            defaultOperation.budgetId,
                                            dateTime.toString(),
                                            defaultOperation.userId,
                                            valueFloat
                                        )
                                    operation?.let {
                                        onOperationEdition( returnOperation )
                                    } ?: kotlin.run {
                                        onOperationCreation( returnOperation )
                                    }
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

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                OutlinedButton(onClick = { openDatePicker = true }) {
                                    Text("change date")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedButton(onClick = { openTimePicker = true }) {
                                    Text("change time")
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

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
                                    Text(text = "PLN") //TODO: handle currencies
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Done
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

    when {
        openDatePicker -> {
            DatePickerDialog(
                datePickerState = datePickerState,
                onApply = {
                    openDatePicker = false
                },
                onDismiss = {
                    openDatePicker = false
                }
            )
        }
        openTimePicker -> {
            TimePickerDialog(
                timePickerState = timePickerState,
                onApply = {
                    openTimePicker = false
                },
                onDismiss = {
                    openTimePicker = false
                }
            )
        }
    }

}