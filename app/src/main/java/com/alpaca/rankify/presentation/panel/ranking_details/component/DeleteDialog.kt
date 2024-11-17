package com.alpaca.rankify.presentation.panel.ranking_details.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.alpaca.rankify.R

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
                painter = painterResource(R.drawable.ic_delete),
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