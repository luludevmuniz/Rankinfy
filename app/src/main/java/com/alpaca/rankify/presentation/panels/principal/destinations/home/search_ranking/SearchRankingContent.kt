package com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking

import android.widget.Space
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.alpaca.rankify.R
import com.alpaca.rankify.presentation.common.PasswordOutlinedTextField
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.RankingNameUiState
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.RankingPasswordUiState
import com.alpaca.rankify.ui.theme.MEDIUM_PADDING

@Composable
fun SearchRankingContent(
    modifier: Modifier = Modifier,
    searchRankingUiState: () -> SearchRankingUiState,
    onIsAdministratorChange: () -> Unit,
    idUiState: () -> RankingNameUiState,
    onRankingIdChange: (String) -> Unit,
    onSearchRankingClick: () -> Unit,
    passwordState: () -> RankingPasswordUiState,
    onRankingPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = idUiState().value,
            onValueChange = onRankingIdChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {
                Text(stringResource(R.string.id_do_ranking))
            },
            placeholder = {
                Text(stringResource(R.string.apenas_numeros))
            },
            singleLine = true,
            prefix = {
                Text("#")
            },
            isError = idUiState().error
        )
        Spacer(modifier = Modifier.height(MEDIUM_PADDING))
        AnimatedVisibility(visible = searchRankingUiState().isAdministrator) {
            PasswordOutlinedTextField(
                passwordState = passwordState,
                onPasswordChange = { password ->
                    onRankingPasswordChange(password)
                },
                onTogglePasswordVisibility = {
                    onTogglePasswordVisibility()
                },
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = searchRankingUiState().isAdministrator,
                onCheckedChange = {
                    onIsAdministratorChange()
                }
            )
            Text(text = "Sou administrador do ranking", style = MaterialTheme.typography.bodyLarge)
        }
        FilledTonalButton(
            modifier = Modifier.align(Alignment.End),
            onClick = onSearchRankingClick
        ) {
            Text(stringResource(R.string.buscar_ranking))
        }
    }
}