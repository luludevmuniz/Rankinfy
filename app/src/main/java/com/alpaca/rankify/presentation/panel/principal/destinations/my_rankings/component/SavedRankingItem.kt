package com.alpaca.rankify.presentation.panel.principal.destinations.my_rankings.component

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.alpaca.rankify.R
import com.alpaca.rankify.domain.model.Ranking
import com.alpaca.rankify.util.constant.TestingTagsConstants.SavedRankingItem.RANKING_NOT_SAVED_TEXT
import com.alpaca.rankify.util.constant.TestingTagsConstants.SavedRankingItem.RANKING_SAVED_TEXT
import com.alpaca.rankify.util.constant.TestingTagsConstants.SavedRankingItem.SAVED_RANKING_ITEM

@Composable
fun SavedRankingItem(
    onClick: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    ranking: Ranking
) {
    ListItem(
        modifier = Modifier
            .combinedClickable(
                onClick = { onClick(ranking.localId) },
                onLongClick = { onDelete(ranking.localId) }
            )
            .testTag(SAVED_RANKING_ITEM),
        headlineContent = {
            Text(
                text = ranking.name,
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingContent = {
            if (ranking.formattedLastUpdated.isBlank()) {
                Text(
                    modifier = Modifier.testTag(RANKING_NOT_SAVED_TEXT),
                    text = stringResource(R.string.o_ranking_ainda_nao_salvo_no_servidor)
                )
            } else {
                Text(
                    modifier = Modifier.testTag(RANKING_SAVED_TEXT),
                    text = stringResource(
                        R.string.atualizado_em,
                        ranking.formattedLastUpdated
                    ),
                )
            }
        }
    )
    HorizontalDivider(modifier = Modifier.fillMaxWidth())
}