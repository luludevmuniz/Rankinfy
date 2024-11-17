package com.alpaca.rankify.presentation.panel.ranking_details.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.alpaca.rankify.R
import com.alpaca.rankify.ui.theme.EXTRA_LARGE_PADDING
import com.alpaca.rankify.ui.theme.MEDIUM_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerDialog(
    modifier: Modifier = Modifier,
    playerName: () -> String,
    playerScore: () -> String,
    title: String,
    icon: Painter,
    isNameWithError: () -> Boolean = { false },
    isScoreWithError: () -> Boolean = { false },
    onPlayerNameChange: (String) -> Unit,
    onPlayerScoreChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    BasicAlertDialog(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 10))
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(all = EXTRA_LARGE_PADDING)
        ,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier.width(IntrinsicSize.Max),
            verticalArrangement = Arrangement.spacedBy(MEDIUM_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            OutlinedTextField(
                value = playerName(),
                onValueChange = {
                    onPlayerNameChange(it)
                },
                label = {
                    Text("Nome")
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_account_box),
                        contentDescription = null
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text
                ),
                isError = isNameWithError()
            )
            OutlinedTextField(
                value = playerScore(),
                onValueChange = {
                    onPlayerScoreChange(it)
                },
                label = {
                    Text("Pontuação")
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_star),
                        contentDescription = null
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = isScoreWithError()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = MEDIUM_PADDING,
                    alignment = Alignment.End
                )
            ) {
                OutlinedButton(
                    onClick = onDismissRequest
                ) {
                    Text(
                        "Cancelar",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                FilledTonalButton(
                    onClick = {
                        onConfirmClick()
                    }
                ) {
                    Text("Confirmar")
                }
            }
        }
    }
}

@Preview
@Composable
fun PlayerDialogPreview() {
    val playerName = remember { mutableStateOf("Player 1") }
    val playerScore = remember { mutableStateOf("100") }
    val isNameWithError = remember { mutableStateOf(false) }
    val isScoreWithError = remember { mutableStateOf(false) }

    PlayerDialog(
        playerName = { playerName.value },
        playerScore = { playerScore.value },
        title = "Add Player",
        icon = painterResource(id = R.drawable.ic_add_circle),
        isNameWithError = { isNameWithError.value },
        isScoreWithError = { isScoreWithError.value },
        onPlayerNameChange = { playerName.value = it },
        onPlayerScoreChange = { playerScore.value = it },
        onConfirmClick = {  },
        onDismissRequest = {  }
    )
}