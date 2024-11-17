package com.alpaca.rankify.presentation.panel.principal.destinations.home.create_ranking

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.alpaca.rankify.R
import com.alpaca.rankify.presentation.common.PasswordOutlinedTextField
import com.alpaca.rankify.presentation.common.RankingIdOutlinedTextField
import com.alpaca.rankify.ui.theme.MEDIUM_PADDING
import com.alpaca.rankify.util.constant.TestingTagsConstants.CreateRanking.CREATE_RANKING_BUTTON
import com.alpaca.rankify.util.constant.TestingTagsConstants.CreateRanking.RANKING_NAME_TEXT_FIELD
import com.alpaca.rankify.util.constant.TestingTagsConstants.CreateRanking.RANKING_PASSWORD_TEXT_FIELD

@Composable
fun CreateRankingContent(
    modifier: Modifier = Modifier,
    nameState: () -> RankingNameUiState,
    passwordState: () -> RankingPasswordUiState,
    onRankingNameChange: (String) -> Unit,
    onRankingPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onCreateClick: () -> Unit,
    isLoading: () -> Boolean = { false }
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = MEDIUM_PADDING)
    ) {
        Text(
            text = stringResource(R.string.novo_ranking),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        RankingIdOutlinedTextField(
            modifier = Modifier.testTag(RANKING_NAME_TEXT_FIELD),
            nameState = nameState,
            onRankingIdChange = { name ->
                onRankingNameChange(name)
            },
            label = {
                Text(text = stringResource(R.string.nome_do_ranking))
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        PasswordOutlinedTextField(
            modifier = Modifier.testTag(RANKING_PASSWORD_TEXT_FIELD),
            passwordState = passwordState,
            onPasswordChange = { password ->
                onRankingPasswordChange(password)
            },
            onTogglePasswordVisibility = {
                onTogglePasswordVisibility()
            },
        )
        CreateRankingButton(
            modifier = Modifier
                .align(Alignment.End)
                .testTag(CREATE_RANKING_BUTTON),
            onCreateClick = {
                onCreateClick()
            },
            isLoading = isLoading
        )
    }
}

@Composable
private fun CreateRankingButton(
    modifier: Modifier = Modifier,
    onCreateClick: () -> Unit,
    isLoading: () -> Boolean
) {
    FilledTonalButton(
        modifier = modifier.animateContentSize(),
        onClick = {
            onCreateClick()
        },
        enabled = isLoading().not()
    ) {
        if (isLoading()) {
            CircularProgressIndicator()
        } else {
            Text(text = stringResource(R.string.criar_ranking))
        }
    }
}