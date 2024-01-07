package com.prax19.budgetguard.app.android.presentation.util

sealed class Screen(val route: String) {

    object MainScreen: Screen("main_screen")
    object BudgetDetailsScreen: Screen("budget_details_screen")

}