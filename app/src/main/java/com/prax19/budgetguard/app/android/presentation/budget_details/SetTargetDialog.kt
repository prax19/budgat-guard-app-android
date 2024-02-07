package com.prax19.budgetguard.app.android.presentation.budget_details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.prax19.budgetguard.app.android.data.model.Target
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SetTargetDialog(
    current: Float,
    oldTarget: Target?,
    onDismiss: () -> Unit,
    onAccept: (target: Target) -> Unit
) {

    val defaultTarget = oldTarget ?: Target(
        startDate = LocalDate.now(),
        startValue = current,
        endDate = LocalDate.now().plusWeeks(1),
        endValue = current
    )

    val isStartDateDialogShown = remember { mutableStateOf(false) }
    val startDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = defaultTarget.startDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000
    )
    val startValue = remember { mutableStateOf(defaultTarget.startValue.toString()) }

    val isEndDateDialogShown = remember { mutableStateOf(false) }
    val endDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = defaultTarget.endDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000
    )
    val endValue = remember { mutableStateOf(defaultTarget.endValue.toString()) }

    when {
        isStartDateDialogShown.value -> {
            DatePickerDialog(
                datePickerState = startDatePickerState,
                onApply = {
                    isStartDateDialogShown.value = false
                },
                onDismiss = {
                    isStartDateDialogShown.value = false
                }
            )
        }
        isEndDateDialogShown.value -> {
            DatePickerDialog(
                datePickerState = endDatePickerState,
                onApply = {
                    isEndDateDialogShown.value = false
                },
                onDismiss = {
                    isEndDateDialogShown.value = false
                }
            )
        }
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        ElevatedCard {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Set your target",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
//                    item {
//                        TextButton(
//                            onClick = {
//                                isStartDateDialogShown.value = true
//                            },
//                            content = {
//                                Text("Select start date")
//                            }
//                        )
//                    }
//                    item {
//                        OutlinedTextField(
//                            value = startValue.value,
//                            label = {
//                                Text(text = "Start value")
//                            },
//                            maxLines = 1,
//                            onValueChange = {
//                                startValue.value = it
//                            }
//                        )
//                    }
                    item {
                        TextButton(
                            onClick = {
                                isEndDateDialogShown.value = true
                            },
                            content = {
                                Text("Select end date")
                            }
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = endValue.value,
                            label = {
                                Text(text = "Target value")
                            },
                            maxLines = 1,
                            onValueChange = {
                                endValue.value = it
                            },
                            suffix = {
                                Text(text = "PLN") //TODO: handle currencies
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Done
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.Right
                ) {
                    TextButton(
                        onClick = onDismiss,
                        content = {
                            Text(text = "Dismiss")
                        }
                    )
                    TextButton(
                        onClick = {
                            val startDate = LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(
                                    startDatePickerState.selectedDateMillis!!
                                ),
                                ZoneOffset.UTC
                            ).toLocalDate()
                            val endDate = LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(
                                    endDatePickerState.selectedDateMillis!!
                                ),
                                ZoneOffset.UTC
                            ).toLocalDate()
                            if(startDate.isBefore(endDate))
                                onAccept(
                                    Target(
                                        startDate = startDate,
                                        startValue.value.toFloat(),
                                        endDate = endDate,
                                        endValue.value.toFloat()
                                    )
                                )
                        },
                        enabled = startValue.value.isNotBlank()
                                && endValue.value.isNotBlank(),
                        content = {
                            Text(text = "Accept")
                        }
                    )
                }
            }
        }
    }
}