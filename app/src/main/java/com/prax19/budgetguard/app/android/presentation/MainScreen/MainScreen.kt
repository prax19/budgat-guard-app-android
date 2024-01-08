package com.prax19.budgetguard.app.android.presentation.MainScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prax19.budgetguard.app.android.data.dto.BudgetDTO
import com.prax19.budgetguard.app.android.presentation.util.Screen
import com.prax19.budgetguard.app.android.previews.BudgetPreviewParameterProvider

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Budget Guard")
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
                .padding(top = it.calculateTopPadding())
        ) {
            val viewModel: MainScreenViewModel = hiltViewModel()
            val budgets = viewModel.state.value.budgets
            val isLoading = viewModel.state.value.isLoading
            budgets?.let {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(128.dp),
                    verticalItemSpacing = 4.dp,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(16.dp),
                    content = {
                        items(budgets) { budget ->
                            BudgetItem(
                                budget = budget,
                                navController
                            )
                        }
                    }
                )
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetItem(
    @PreviewParameter(BudgetPreviewParameterProvider::class) budget: BudgetDTO,
    navController: NavController
) {
    ElevatedCard(
        onClick = {
                  navController.navigate(Screen.BudgetDetailsScreen.route +
                          "?budgetId=${budget.id}")
        },
        content = {
            Text(
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(16.dp),
                text = budget.name,
                textAlign = TextAlign.Start
            )
        }
    )
}