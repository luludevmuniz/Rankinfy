package com.alpaca.rankify.presentation.panels.ranking_details.component

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.alpaca.rankify.ui.theme.EXTRA_LARGE_PADDING
import com.alpaca.rankify.ui.theme.MEDIUM_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerDialog(
    modifier: Modifier = Modifier,
    playerName: () -> String,
    playerScore: () -> String,
    title: String,
    icon: ImageVector,
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
                imageVector = icon,
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
                        imageVector = Icons.Default.AccountBox,
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
                        imageVector = Icons.Default.Star,
                        contentDescription = null
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = isScoreWithError()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround) {
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

//@Preview(showBackground = true)
//@Composable
//private fun CreatePlayerDialogPrev() {
//    RankifyTheme {
//        PlayerDialog(
//            playerName = "",
//            playerScore = "",
//            title = "Criar jogador",
//            icon = Icons.Default.AddCircle,
//            isNameWithError = false,
//            isScoreWithError = false,
//            onPlayerNameChange = {},
//            onPlayerScoreChange = {},
//            onConfirmClick = {},
//            onDismissRequest = {}
//        )
//    }
//}