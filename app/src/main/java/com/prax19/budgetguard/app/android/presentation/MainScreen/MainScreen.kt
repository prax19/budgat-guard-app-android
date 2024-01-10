package com.prax19.budgetguard.app.android.presentation.MainScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prax19.budgetguard.app.android.data.dto.BudgetDTO
import com.prax19.budgetguard.app.android.presentation.ContextActions
import com.prax19.budgetguard.app.android.presentation.Selectable
import com.prax19.budgetguard.app.android.util.Screen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavController) {

    val viewModel: MainScreenViewModel = hiltViewModel()
    val budgets = viewModel.state.value.budgets
    val isLoading = viewModel.state.value.isLoading

    val openAddEditBudget = remember { mutableStateOf(false) }

    var contextActionsBudgetId by rememberSaveable { mutableStateOf<Long?>(null) }

    val onCloseContextAction: () -> Unit = {
        contextActionsBudgetId = null
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
                        },
                        contextActionsBudgetId != null
                    )
                }
            )
        },
        floatingActionButton = {
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
        },
        content = {

            when {
                openAddEditBudget.value ->
                    AddEditBudgetDialog(
                        onBudgetCreation = { budget ->
                            viewModel.createNewBudget(
                                BudgetDTO(
                                    budget.id,
                                    budget.name,
                                    budget.ownerId,
                                    budget.operations
                                ) //TODO: handle user
                            )
                            openAddEditBudget.value = false
                            onCloseContextAction()
                        },
                        onBudgetEdition = { budget ->
                            viewModel.editBudget(
                                BudgetDTO(
                                    budget.id,
                                    budget.name,
                                    budget.ownerId,
                                    budget.operations
                                ) //TODO: handle user
                            )
                            openAddEditBudget.value = false
                            onCloseContextAction()
                        },
                        onDismissRequest = {
                            openAddEditBudget.value = false
                            onCloseContextAction()
                        },
                        budget = viewModel.getBudgetById(contextActionsBudgetId)
                    )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = it.calculateBottomPadding())
                    .padding(top = it.calculateTopPadding())
            ) {
                budgets?.let {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Adaptive(128.dp),
                        verticalItemSpacing = 4.dp,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(16.dp),
                        content = {
                            items(budgets) { budget ->
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
    )

}

@Composable
fun BudgetItem( //TODO: add more details to items
    budget: BudgetDTO,
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
            Text(
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(16.dp),
                text = budget.name,
                textAlign = TextAlign.Start
            )
        }
    }
}