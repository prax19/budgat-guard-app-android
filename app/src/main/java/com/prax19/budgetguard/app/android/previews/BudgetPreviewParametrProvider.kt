package com.prax19.budgetguard.app.android.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.prax19.budgetguard.app.android.data.dto.BudgetDTO

class BudgetPreviewParameterProvider: PreviewParameterProvider<BudgetDTO> {

    override val values = sequenceOf(BudgetDTO(1, "Example budget", 1, emptyList()))

}