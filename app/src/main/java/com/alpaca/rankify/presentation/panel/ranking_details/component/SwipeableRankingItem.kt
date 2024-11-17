package com.alpaca.rankify.presentation.panel.ranking_details.component

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alpaca.rankify.domain.model.Player
import com.alpaca.rankify.presentation.common.SwipeBox
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

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RankingItemPrev() {
    RankifyTheme {
    }
}