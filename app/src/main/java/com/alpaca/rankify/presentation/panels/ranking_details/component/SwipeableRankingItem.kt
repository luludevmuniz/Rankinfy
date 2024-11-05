package com.alpaca.rankify.presentation.panels.ranking_details.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alpaca.rankify.R
import com.alpaca.rankify.domain.model.Player
import com.alpaca.rankify.presentation.common.SwipeBox
import com.alpaca.rankify.ui.theme.EXTRA_LARGE_PADDING
import com.alpaca.rankify.ui.theme.MEDIUM_PADDING
import com.alpaca.rankify.ui.theme.RankifyTheme

@Composable
fun SwipeableRankingItem(
    modifier: Modifier = Modifier,
    player: () -> Player,
    onEdit: (Player) -> Unit,
    onDelete: (Player) -> Unit
) {
    val updatedOnEditPlayer by rememberUpdatedState(onEdit)
    val updatedOnDeletePlayer by rememberUpdatedState(onDelete)

    SwipeBox(
        modifier = modifier,
        onDelete = {
            updatedOnDeletePlayer(player())
        },
        onEdit = {
            updatedOnEditPlayer(player())
        }
    ) {
        RankingItem(player = player)
    }
}

@Composable
fun RankingItem(
    modifier: Modifier = Modifier,
    player: () -> Player,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MEDIUM_PADDING)
    ) {
        Card(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = MEDIUM_PADDING,
                        horizontal = EXTRA_LARGE_PADDING
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(
                        R.string.posicao_ranking,
                        player().currentRankingPosition
                    )
                )
                Text(text = player().name)
                Text(text = player().score)
            }
        }
        if (player().currentRankingPosition != 0) {
            val rankingIcon =
                if (player().currentRankingPosition < player().previousRankingPosition)
                    Icons.Default.KeyboardArrowUp else
                    Icons.Default.KeyboardArrowDown
            Icon(
                imageVector = rankingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RankingItemPrev() {
    RankifyTheme {
    }
}