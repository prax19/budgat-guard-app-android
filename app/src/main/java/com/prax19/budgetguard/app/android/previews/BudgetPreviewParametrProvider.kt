package com.prax19.budgetguard.app.android.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.prax19.budgetguard.app.android.data.Budget

class BudgetPreviewParameterProvider: PreviewParameterProvider<Budget> {

    override val values = sequenceOf(Budget(1, "Example budget", 1, emptyList()))

}