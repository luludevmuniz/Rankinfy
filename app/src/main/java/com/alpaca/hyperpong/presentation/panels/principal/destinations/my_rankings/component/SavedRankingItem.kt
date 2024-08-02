package com.alpaca.hyperpong.presentation.panels.principal.destinations.my_rankings.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alpaca.hyperpong.domain.model.Ranking

@Composable
fun SavedRankingItem(
    onClick: (Long) -> Unit,
    ranking: Ranking
) {
    ListItem(
        modifier = Modifier.clickable {
            onClick(ranking.localId)
        },
        headlineContent = {
            Text(
                text = ranking.name,
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingContent = {
            if (ranking.formattedLastUpdated.isBlank()) {
                Text(text = "O ranking ainda não salvo no servidor")
            } else {
                Text(
                    text = "Atualizado em ${ranking.formattedLastUpdated}",
                )
            }
        }
    )
    HorizontalDivider(modifier = Modifier.fillMaxWidth())
}