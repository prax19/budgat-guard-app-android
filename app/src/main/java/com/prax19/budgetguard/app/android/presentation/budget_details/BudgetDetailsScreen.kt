package com.prax19.budgetguard.app.android.presentation.budget_details

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.prax19.budgetguard.app.android.data.auth.AuthResult
import com.prax19.budgetguard.app.android.data.model.Operation
import com.prax19.budgetguard.app.android.presentation.utils.ContextActions
import com.prax19.budgetguard.app.android.presentation.utils.Selectable
import com.prax19.budgetguard.app.android.util.Screen
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BudgetDetailsScreen(navController: NavController) {
    val viewModel: BudgetDetailsScreenViewModel = hiltViewModel()
    val budget = viewModel.state.value.budget
    val target = viewModel.state.value.target
    val operations = viewModel.state.value.opsToDisplay
    val isLoading = viewModel.state.value.isLoading
    val isViewReady = viewModel.state.value.isViewReady

    val viewScope = rememberCoroutineScope()

    val scaffoldState = rememberBottomSheetScaffoldState()

    val openAddEditOperation = remember { mutableStateOf(false) }
    val openSetTarget = remember { mutableStateOf(false) }

    //TODO: selection of multiple items
    var contextActionsOperationId by rememberSaveable { mutableStateOf<Long?>(null) }

    val onCloseContextAction: () -> Unit = {
        contextActionsOperationId = null
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState = lifecycleOwner.lifecycle.currentState

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.CREATED -> {
                viewModel.markViewAsNotReady()
            }

            Lifecycle.State.RESUMED -> {
                viewModel.loadBudget()
            }

            else -> {}
        }
    }

    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.results.collect {
            when (it) {
                is AuthResult.Unauthorized -> {
                    Toast.makeText(
                        context,
                        "User unauthorized!",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(Screen.SignInScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }

                is AuthResult.Forbidden -> {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(Screen.SignInScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }

                is AuthResult.Error -> {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(Screen.SignInScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }

                is AuthResult.UserNotFound -> {}
                is AuthResult.Authorized -> {}
            }
        }
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
                                LocalDateTime.parse(operation.dateTime),
                                budget.ownerId,
                                operation.value
                            ) //TODO: handle user
                        )
                        openAddEditOperation.value = false
                        viewModel.markViewAsNotReady()
                        viewScope.launch {
                            scaffoldState.bottomSheetState.partialExpand()
                        }
                        onCloseContextAction()
                    },
                    onOperationEdition = { operation ->
                        viewModel.editOperation(
                            Operation(
                                operation.id,
                                operation.name,
                                budget,
                                LocalDateTime.parse(operation.dateTime),
                                budget.ownerId,
                                operation.value
                            ) //TODO: handle user
                        )
                        openAddEditOperation.value = false
                        viewModel.markViewAsNotReady()
                        viewScope.launch {
                            scaffoldState.bottomSheetState.partialExpand()
                        }
                        onCloseContextAction()
                    },
                    onDismissRequest = {
                        openAddEditOperation.value = false
                        onCloseContextAction()
                    },
                    operation = viewModel.getOperationById(contextActionsOperationId)
                )

            openSetTarget.value -> {
                SetTargetDialog(
                    current = budget.balance ?: 0f,
                    oldTarget = target,
                    onDismiss = {
                        openSetTarget.value = false
                    },
                    onAccept = {
                        openSetTarget.value = false
                        viewModel.setTarget(it)
                    }
                )
            }
        }
    }
    BottomSheetScaffold(
        modifier = Modifier
            .pointerInput(onCloseContextAction) { detectTapGestures { onCloseContextAction() } },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 100.dp,
        sheetTonalElevation = 2.dp,
        sheetShadowElevation = 2.dp,
        sheetSwipeEnabled = true,
        topBar = {
            TopAppBar(
                title = {
                    AnimatedVisibility(
                        enter = fadeIn(),
                        exit = fadeOut(),
                        visible = isViewReady
                    ) {
                        budget?.let {
                            Text(
                                text = budget.name,
                                maxLines = 1
                            )
                        }
                    }
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
                                viewModel.deleteOperation(
                                    viewModel.getOperationById(
                                        contextActionsOperationId
                                    )!!
                                )
                            }
                            contextActionsOperationId = null
                        },
                        contextActionsOperationId != null
                    )
                }
            )
        },
        sheetContent = {
            BudgetDetailsBottomSheetContent(
                onClickAddOperation = {
                    openAddEditOperation.value = true
                    onCloseContextAction()
                },
                onClickSetTarget = {
                    openSetTarget.value = true
                },
                content = {
                    target?.let {
                        Divider(Modifier.padding(vertical = 8.dp))
                        val advice = target.getAdvice(budget!!.balance!!, LocalDate.now()) ?: 0f
                        when {
                            advice < 0f -> {
                                Text(
                                    text = "You can spend %.2f %s a day.".format(
                                        advice.absoluteValue,
                                        budget.currency.symbol
                                    )
                                )
                            }

                            else -> {
                                Text(
                                    text = "You have to save %.2f %s a day.".format(
                                        advice,
                                        budget.currency.symbol
                                    )
                                )
                            }
                        }
                    }
                }
            )
        },
        content = {
            AnimatedVisibility(
                enter = fadeIn(),
                exit = fadeOut(),
                visible = isViewReady
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(viewModel.operationFilters) {
                            FilterChip(
                                selected = it == viewModel.state.value.filter,
                                onClick = {
                                    viewModel.setFilter(it)
                                    viewModel.loadBudget()
                                },
                                label = { Text(it.text) })
                        }
                    }
                    budget?.let {
                        when (operations.isEmpty()) {
                            true -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Budget is empty!")
                                }
                            }

                            false -> {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(16.dp),
                                    content = {
                                        item {
                                            Column(
                                                modifier = Modifier
                                                    .padding(horizontal = 24.dp)
                                                    .padding(bottom = 8.dp)
                                            ) {
                                                Text(
                                                    text = "Balance: %.2f %s".format(
                                                        budget.balance,
                                                        budget.currency.symbol
                                                    ),
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                AnimatedVisibility(visible = viewModel.state.value.filter != OperationsFilter.All) {
                                                    Text(
                                                        text = "%s: %s".format(
                                                            viewModel.state.value.filter.text.lowercase(),
                                                            when {
                                                                isLoading -> "-"
                                                                else -> "%.2f %s"
                                                                    .format(
                                                                        viewModel.state.value.filteredBalance,
                                                                        budget.currency.symbol
                                                                    )
                                                            }
                                                        ),
                                                        style = MaterialTheme.typography.titleSmall,
                                                        color = when {
                                                            viewModel.state.value.filteredBalance < 0 -> {
                                                                MaterialTheme.colorScheme.error
                                                            }

                                                            else -> {
                                                                MaterialTheme.colorScheme.primary
                                                            }
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                        items(operations) { operation ->
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
                    }
                }
            }
            if (isLoading)
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
        }
    )
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
            .fillMaxWidth()
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
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1
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
                        text = "%.2f %s".format(
                            operation.value,
                            operation.budget.currency.symbol
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = color,
                        maxLines = 1
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
//                    Text(
//                        text = "Category",
//                        style = MaterialTheme.typography.bodySmall
//                    )
//                    Text(
//                        text = "prax19 • ${operation.dateTime.toLocalDate()}",
//                        style = MaterialTheme.typography.bodySmall
//                    )
                    Text(
                        text = "%s • %s".format(
                            operation.dateTime.toLocalTime(),
                            operation.dateTime.toLocalDate()
                        ),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun BudgetDetailsBottomSheetContent(
    onClickAddOperation: () -> Unit,
    onClickSetTarget: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = onClickSetTarget,
                content = {
                    Icon(
                        Icons.Outlined.Star,
                        "Set target"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Set target")
                }
            )
            OutlinedButton(
                onClick = onClickAddOperation,
                content = {
                    Icon(
                        Icons.Filled.Add,
                        "Add new operation"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add operation")
                }
            )
        }
        content()
    }
}