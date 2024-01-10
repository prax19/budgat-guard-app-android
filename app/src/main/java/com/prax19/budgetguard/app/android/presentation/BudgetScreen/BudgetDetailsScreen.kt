package com.prax19.budgetguard.app.android.presentation.BudgetScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.prax19.budgetguard.app.android.data.model.Operation
import com.prax19.budgetguard.app.android.presentation.ContextActions
import com.prax19.budgetguard.app.android.presentation.Selectable

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BudgetDetailsScreen() {
    val viewModel: BudgetDetailsScreenViewModel = hiltViewModel()
    val budget = viewModel.budgetState.value.budget
    val isLoading = viewModel.budgetState.value.isLoading

    val openAddEditOperation = remember { mutableStateOf(false) }

    //TODO: selection of multiple items
    var contextActionsOperationId by rememberSaveable { mutableStateOf<Long?>(null) }

    val onCloseContextAction: () -> Unit = {
        contextActionsOperationId = null
    }

    if(isLoading)
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }


    budget?.let {
        when {
            openAddEditOperation.value ->
                AddEditOperationDialog(
                    onOperationCreation = { operation ->
                        viewModel.createOperation(
                            Operation(
                                operation.id,
                                operation.name,
                                budget,
                                budget.ownerId,
                                operation.value
                            ) //TODO: handle user
                        )
                        openAddEditOperation.value = false
                        onCloseContextAction()
                    },
                    onOperationEdition = {operation ->
                        viewModel.editOperation(
                            Operation(
                                operation.id,
                                operation.name,
                                budget,
                                budget.ownerId,
                                operation.value
                            ) //TODO: handle user
                        )
                        openAddEditOperation.value = false
                        onCloseContextAction()
                    },
                    onDismissRequest = {
                        openAddEditOperation.value = false
                        onCloseContextAction()
                    },
                    operation = viewModel.getOperation(contextActionsOperationId)
                )
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(onCloseContextAction) { detectTapGestures { onCloseContextAction() } },
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = budget.name)
                    },
                    actions = {
                        ContextActions(
                            onClickEdit = {
                                contextActionsOperationId?.let {
                                    openAddEditOperation.value = true
                                }
                            },
                            onClickDelete = {
                                contextActionsOperationId?.let {
                                    //TODO: add operation deletion dialog
                                    viewModel.deleteOperation(it)
                                }
                            },
                            contextActionsOperationId != null
                        )
                    }
                )
            },
            floatingActionButton =
            {
                FloatingActionButton(
                    onClick = {
                        openAddEditOperation.value = true
                        onCloseContextAction()
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
                        .padding(top = it.calculateTopPadding()),
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(16.dp),
                        content = {
                            items(budget.operations) { operation ->
                                BudgetOperationItem(
                                    operation = operation,
                                    onClick = {
                                        onCloseContextAction()
                                        //TODO: operation details screen
                                    },
                                    onLongClick = {
                                        contextActionsOperationId = operation.id
                                    },
                                    selected = contextActionsOperationId == operation.id
                                )
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
    operation: Operation,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    selected: Boolean
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Selectable(
            selected = selected,
            onClick = onClick,
            onLongClick = onLongClick
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
}