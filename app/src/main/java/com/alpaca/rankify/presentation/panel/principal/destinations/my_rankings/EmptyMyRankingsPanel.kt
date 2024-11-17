package com.alpaca.rankify.presentation.panel.principal.destinations.my_rankings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alpaca.rankify.R
import com.alpaca.rankify.presentation.anim.GhostAnimation
import com.alpaca.rankify.ui.theme.RankifyTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EmptyMyRankingsPanel() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_rankings_yet),
            style = MaterialTheme.typography.headlineLargeEmphasized,
            color = MaterialTheme.colorScheme.onBackground
        )
        GhostAnimation()
    }
}

@Preview(showSystemUi = true)
@Composable
private fun EmptyMyRankingsPanelPrev() {
    RankifyTheme {
        EmptyMyRankingsPanel()
    }
}