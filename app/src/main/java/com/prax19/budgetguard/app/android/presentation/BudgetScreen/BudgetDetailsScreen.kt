package com.prax19.budgetguard.app.android.presentation.BudgetScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BudgetDetailsScreen(
    navController: NavController
) {
    val viewModel: BudgetDetailsScreenViewModel = hiltViewModel()
    val budget = viewModel.budgetState.value.budget
    val isLoading = viewModel.budgetState.value.isLoading
    budget?.let {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = budget.name)
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
                for(op: Long in budget.operations)
                    Text(op.toString())
            }
        }
    }
}