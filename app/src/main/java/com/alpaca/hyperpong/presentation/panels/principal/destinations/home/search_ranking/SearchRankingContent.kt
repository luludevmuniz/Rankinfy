package com.alpaca.hyperpong.presentation.panels.principal.destinations.home.search_ranking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alpaca.hyperpong.ui.theme.MEDIUM_PADDING

@Composable
fun SearchRankingContent(
    modifier: Modifier = Modifier,
    rankingName: String,
    rankingNameError: Boolean,
    rankingIdError: Boolean,
    rankingId: String,
    onRankingNameChange: (String) -> Unit,
    onRankingIdChange: (String) -> Unit,
    onSearchRankingClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            MEDIUM_PADDING,
            Alignment.CenterVertically
        )
    ) {
        OutlinedTextField(
            value = rankingName,
            onValueChange = onRankingNameChange,
            label = {
                Text("Nome do ranking")
            },
            singleLine = true,
            isError = rankingNameError
        )
        OutlinedTextField(
            value = rankingId,
            onValueChange = onRankingIdChange,
            label = {
                Text("ID do ranking")
            },
            placeholder = {
                Text("Apenas números")
            },
            singleLine = true,
            prefix = {
                Text("#")
            },
            isError = rankingIdError
        )
        FilledTonalButton(
            onClick = onSearchRankingClick
        ) {
            Text("Buscar ranking")
        }
    }
}