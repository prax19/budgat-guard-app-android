package com.prax19.budgetguard.app.android.presentation.main_screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.prax19.budgetguard.app.android.data.auth.AuthResult
import com.prax19.budgetguard.app.android.data.model.Budget
import com.prax19.budgetguard.app.android.presentation.utils.ContextActions
import com.prax19.budgetguard.app.android.presentation.utils.Selectable
import com.prax19.budgetguard.app.android.util.Screen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavController) {

    val viewModel: MainScreenViewModel = hiltViewModel()
    val budgets = viewModel.state.value.budgets
    val isLoading = viewModel.state.value.isLoading
    val isViewReady = viewModel.state.value.isViewReady

    val openAddEditBudget = remember { mutableStateOf(false) }

    var contextActionsBudgetId by rememberSaveable { mutableStateOf<Long?>(null) }

    val onCloseContextAction: () -> Unit = {
        contextActionsBudgetId = null
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState = lifecycleOwner.lifecycle.currentState

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.CREATED -> {
                viewModel.markViewAsNotReady()
            }
            Lifecycle.State.RESUMED -> {
                viewModel.loadBudgets()
            }
            else -> {}
        }
    }

    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.results.collect {
            when(it) {
                is AuthResult.Unauthorized -> {
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
                    navController.navigate(Screen.MainScreen.route) {
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

    Scaffold(
        modifier = Modifier
            .pointerInput(onCloseContextAction) { detectTapGestures { onCloseContextAction() } },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Budget Guard")
                },
                actions = {
                    ContextActions(
                        onClickEdit = {
                            contextActionsBudgetId?.let {
                                openAddEditBudget.value = true
                            }
                        },
                        onClickDelete = {
                            contextActionsBudgetId?.let {
                                //TODO: add budget deletion dialog
                                viewModel.deleteBudget(viewModel.getBudgetById(it)!!)
                            }
                            contextActionsBudgetId = null
                        },
                        contextActionsBudgetId != null
                    )
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.SignInScreen.route) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        },
                        content = {
                            Icon(Icons.Filled.Logout, "Log out")
                        }
                    )
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                enter = scaleIn(),
                exit = scaleOut(),
                visible = isViewReady,
                content = {
                    FloatingActionButton(
                        onClick = {
                            openAddEditBudget.value = true
                            onCloseContextAction()
                        },
                        content = {
                            Icon(
                                Icons.Filled.Add,
                                "Add new budget"
                            )
                        }
                    )
                }
            )
        },
        content = {
            when {
                openAddEditBudget.value ->
                    AddEditBudgetDialog(
                        onBudgetCreation = { budget ->
                            viewModel.createNewBudget(budget)
                            openAddEditBudget.value = false
                            viewModel.markViewAsNotReady()
                            onCloseContextAction()
                        },
                        onBudgetEdition = { budget ->
                            viewModel.editBudget(budget)
                            openAddEditBudget.value = false
                            viewModel.markViewAsNotReady()
                            onCloseContextAction()
                        },
                        onDismissRequest = {
                            openAddEditBudget.value = false
                            onCloseContextAction()
                        },
                        budget = viewModel.getBudgetById(contextActionsBudgetId)
                    )
            }
            if(isLoading)
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .padding(top = it.calculateTopPadding())
            ) {
                budgets?.let {
                    AnimatedVisibility(visible = isViewReady) {
                        when(it.isEmpty()) {
                            true -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "No budgets!")
                                }
                            }
                            false -> {
                                LazyVerticalStaggeredGrid(
                                    columns = StaggeredGridCells.Adaptive(128.dp),
                                    verticalItemSpacing = 4.dp,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    contentPadding = PaddingValues(16.dp),
                                    content = {
                                        items(it) { budget ->
                                            BudgetItem(
                                                budget,
                                                onClick = {
                                                    onCloseContextAction()
                                                    navController.navigate(
                                                        Screen.BudgetDetailsScreen.route +
                                                                "?budgetId=${budget.id}"
                                                    )
                                                },
                                                onLongClick = {
                                                    contextActionsBudgetId = budget.id
                                                },
                                                selected = contextActionsBudgetId == budget.id
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    )

}

@Composable
fun BudgetItem( //TODO: add more details to items
    budget: Budget,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    selected: Boolean
) {
    ElevatedCard{
        Selectable(
            selected = selected,
            onClick = onClick,
            onLongClick = onLongClick
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.titleSmall,
                    text = budget.name,
                    textAlign = TextAlign.Start
                )
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = "%.2f %s".format(budget.balance, budget.currency.symbol),
                    textAlign = TextAlign.Start,
                    maxLines = 1
                )
            }
        }
    }
}