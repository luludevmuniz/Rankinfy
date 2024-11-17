package com.alpaca.rankify.presentation.common

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alpaca.rankify.presentation.panel.principal.destinations.home.create_ranking.RankingNameUiState

@Composable
fun RankingIdOutlinedTextField(
    modifier: Modifier = Modifier,
    nameState: () -> RankingNameUiState,
    onRankingIdChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        modifier = modifier,
        value = nameState().value,
        onValueChange = { name ->
            onRankingIdChange(name)
        },
        label = label,
        placeholder = placeholder,
        prefix = prefix,
        singleLine = true,
        isError = nameState().error,
        keyboardOptions = keyboardOptions
    )
}