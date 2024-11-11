package com.alpaca.rankify.presentation.panels.principal.destinations.my_rankings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alpaca.rankify.navigation.RankingDestination

@Composable
fun MyRankingsListPanel(
    modifier: Modifier = Modifier,
    myRankingsViewModel: MyRankingsViewModel = hiltViewModel(),
    onRankingClicked: (RankingDestination) -> Unit
) {
    val rankings by myRankingsViewModel.savedRankings.collectAsStateWithLifecycle()
    Scaffold(modifier = modifier) { paddingValues ->
        if (rankings.isEmpty()) {
            EmptyMyRankingsPanel()
        } else {
            MyRankingsContent(
                modifier = Modifier.padding(paddingValues = paddingValues),
                rankings = rankings,
                onClick = { id ->
                    onRankingClicked(RankingDestination(id = id))
                },
                onDelete = { id ->
                    myRankingsViewModel.deleteRanking(id)
                }
            )
        }
    }
}