package com.alpaca.hyperpong.presentation.panels.principal.destinations.my_rankings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alpaca.hyperpong.domain.model.Ranking
import com.alpaca.hyperpong.presentation.panels.principal.destinations.my_rankings.component.SavedRankingItem

@Composable
fun MyRankingsContent(
    modifier: Modifier = Modifier,
    rankings: List<Ranking>,
    onClick: (Long) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(
            items = rankings,
            key = { ranking ->
                ranking.localId
            }
        ) { ranking ->
            SavedRankingItem(
                onClick,
                ranking
            )
        }
    }
}