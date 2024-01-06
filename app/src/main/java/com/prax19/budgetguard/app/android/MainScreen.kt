package com.prax19.budgetguard.app.android

import android.annotation.SuppressLint
import android.content.res.Resources.Theme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.prax19.budgetguard.app.android.data.Budget
import com.prax19.budgetguard.app.android.previews.BudgetPreviewParameterProvider
import java.time.format.TextStyle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
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