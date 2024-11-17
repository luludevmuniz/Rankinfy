package com.alpaca.rankify.presentation.panel.principal.destinations.home.create_ranking.component

import androidx.compose.animation.animateContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.alpaca.rankify.R
import com.alpaca.rankify.util.constant.TestingTagsConstants.CreateRanking.CREATE_RANKING_BUTTON

@Composable
fun CreateRankingButton(
    modifier: Modifier = Modifier,
    onCreateClick: () -> Unit,
    isLoading: () -> Boolean
) {
    FilledTonalButton(
        modifier = modifier
            .animateContentSize()
            .testTag(CREATE_RANKING_BUTTON),
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