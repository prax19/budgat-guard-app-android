package com.prax19.budgetguard.app.android

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.prax19.budgetguard.app.android.data.Budget

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
                for (budget: Budget in budgets)
                    Text(text = budget.name)
            }

        }
    }

}