package com.prax19.budgetguard.app.android.presentation.BudgetScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prax19.budgetguard.app.android.data.model.Operation
import com.prax19.budgetguard.app.android.presentation.AddEditBudgetOperationScreen.AddEditOperationScreen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BudgetDetailsScreen(
    navController: NavController
) {
    val viewModel: BudgetDetailsScreenViewModel = hiltViewModel()
    val budget = viewModel.budgetState.value.budget
    val isLoading = viewModel.budgetState.value.isLoading

    val openAddEditOperation = remember { mutableStateOf(false) }

    budget?.let {

        when { openAddEditOperation.value ->
            AddEditOperationScreen(
                onSave = { name, value ->
                    viewModel.createOperation(
                        Operation(-1, name, budget, budget.ownerId, value) //TODO: handle user
                    )
                    openAddEditOperation.value = false
                },
                onDismissRequest = {
                    openAddEditOperation.value = false
                }
            )
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = budget.name)
                    }
                )
            },
            floatingActionButton =
            {
                FloatingActionButton(
                    onClick = {
                        openAddEditOperation.value = true
                    },
                    content = {
                        Icon(
                            Icons.Filled.Add,
                            "Add new operation"
                        )
                    }
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = it.calculateBottomPadding())
                        .padding(top = it.calculateTopPadding())
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        content = {
                            items(budget.operations) { operation ->
                                BudgetOperationItem(
                                    navController = navController,
                                    operation = operation)
                            }
                        }
                    )
                }
            }
        )
    }
}

@Composable
fun BudgetOperationItem(
    navController: NavController,
    operation: Operation
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.75f),
                ) {
                    Text(
                        text = operation.name,
                        style = MaterialTheme.typography.titleMedium
                    )

//                    Text(
//                        text = "Short transaction description. Short transaction description. Short transaction description. Short transaction description. Short transaction description. Short transaction description. Short transaction description.",
//                        style = MaterialTheme.typography.bodySmall,
//                        maxLines = 3
//                    )
                }
                val color =
                    if(operation.value < 0)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary
                Text(
                    text = "${"%.2f".format(operation.value)} zł",
                    style = MaterialTheme.typography.bodyMedium,
                    color = color
                )
            }
            Divider(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(bottom = 4.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "prax19 • 16.12.2018",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun AddEditBudgetOperationDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            content = {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Add budget operation",
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge
                    )
                    OutlinedTextField(
                        value = "s",
                        onValueChange = {}
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Absolute.Right
                    ) {
                        TextButton(
                            onClick = onDismissRequest,
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Text(text = "Cancel")
                        }
                        TextButton(
                            onClick = onConfirmation,
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Text(text = "Add")
                        }
                    }
                }
            }
        )
    }
}