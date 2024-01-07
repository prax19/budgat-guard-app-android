package com.prax19.budgetguard.app.android.views.MainScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.prax19.budgetguard.app.android.data.Budget
import com.prax19.budgetguard.app.android.previews.BudgetPreviewParameterProvider

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
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
            budgets.let {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier.
                        padding(16.dp),
                    columns = StaggeredGridCells.Adaptive(128.dp),
                    verticalItemSpacing = 4.dp,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    content = {
                        items(budgets) { budget ->
                            BudgetItem(budget = budget)
                        }
                    }
                )
            }

        }
    }

}

@Preview
@Composable
fun BudgetItem(
    @PreviewParameter(BudgetPreviewParameterProvider::class) budget: Budget
) {
    ElevatedCard(

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