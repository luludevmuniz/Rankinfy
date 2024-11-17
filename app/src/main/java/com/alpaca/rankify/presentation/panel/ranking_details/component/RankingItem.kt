package com.alpaca.rankify.presentation.panel.ranking_details.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.alpaca.rankify.R
import com.alpaca.rankify.domain.model.Player
import com.alpaca.rankify.ui.theme.EXTRA_LARGE_PADDING
import com.alpaca.rankify.ui.theme.MEDIUM_PADDING

@Composable
fun RankingItem(
    modifier: Modifier = Modifier,
    player: () -> Player,
) {
    val rankingPositionIcon =
        if (player().previousRankingPosition == 0 || player().currentRankingPosition == player().previousRankingPosition)
            null
        else if (player().currentRankingPosition < player().previousRankingPosition)
            painterResource(R.drawable.ic_arrow_up)
        else painterResource(R.drawable.ic_arrow_down)
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
        rankingPositionIcon?.let { icon ->
            Icon(
                painter = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}