package com.alpaca.rankify.presentation.panels.ranking_details.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DeleteDialog(
    modifier: Modifier = Modifier,
    title: String,
    text: String,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = title)
        },
        icon = {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null
            )
        },
        text = {
            Text(text = text)
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = onConfirmClick

            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest
            ) {
                Text("Cancelar")
            }
        }
    )
}