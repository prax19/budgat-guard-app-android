package com.prax19.budgetguard.app.android.presentation.budget_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    datePickerState: DatePickerState,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )

    ) {
        ElevatedCard{
            Column(
                modifier = Modifier
                    .padding(4.dp)
            ) {
                DatePicker(
                    modifier = Modifier.fillMaxWidth(),
                    state = datePickerState,
                    showModeToggle = false
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.Absolute.Right
                ) {
                    TextButton(
                        onClick = { onDismiss() },
                        content = {
                            Text("Dismiss")
                        }
                    )
                    TextButton(
                        onClick = { onApply() },
                        enabled = datePickerState.selectedDateMillis != null,
                        content = {
                            Text("Apply")
                        }
                    )
                }
            }
        }
    }

}