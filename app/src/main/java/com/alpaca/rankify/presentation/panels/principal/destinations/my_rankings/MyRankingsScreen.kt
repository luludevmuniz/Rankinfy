package com.alpaca.rankify.presentation.panels.principal.destinations.my_rankings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alpaca.rankify.ui.theme.RankifyTheme

@Composable
fun MyRankingsScreen(
    myRankingsViewModel: MyRankingsViewModel = hiltViewModel(),
    onRankingClicked: (Long) -> Unit
) {
    val rankings by myRankingsViewModel.savedRankings.collectAsStateWithLifecycle()
    Scaffold { paddingValues ->
        if (rankings.isEmpty()) {
            EmptyMyRankingsScreen()
        } else {
            MyRankingsContent(
                modifier = Modifier.padding(paddingValues = paddingValues),
                rankings = rankings,
                onClick = { id ->
                    onRankingClicked(id)
                },
                onDelete = { id ->
                    myRankingsViewModel.deleteRanking(id)
                }
            )
        }
    }
}

@Composable
fun EmptyMyRankingsScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.List,
            contentDescription = "Empty Rankings",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        // Text
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No rankings yet",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun EmptyMyRankingsScreenPrev() {
    RankifyTheme {
        EmptyMyRankingsScreen()
    }
}
