package com.prax19.budgetguard.app.android.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Selectable(
    selected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    content: @Composable () -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        color =
        if(selected)
            MaterialTheme.colorScheme.surfaceVariant
        else
            MaterialTheme.colorScheme.surface,
        content = content
    )

}

@Composable
fun ContextActions( // TODO: show composable animation
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
    show: Boolean
) {
    if(show) {
        IconButton(
            onClick = onClickEdit,
            content = {
                Icon(
                    Icons.Filled.Edit,
                    "edit selected")
            }
        )
        IconButton(
            onClick = onClickDelete,
            content = {
                Icon(
                    Icons.Filled.Delete,
                    "delete selected")
            }
        )
    }
}