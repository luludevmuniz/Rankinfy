package com.alpaca.rankify.presentation.panels.principal.destinations.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alpaca.rankify.navigation.TabsDestinations
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingContent
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingUiState
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingContent
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingUiState

@Composable
@NonRestartableComposable
@OptIn(ExperimentalMaterial3Api::class)
 fun HomeContent(
    modifier: Modifier = Modifier,
    createRankingUiState: CreateRankingUiState,
    searchRankingUiState: SearchRankingUiState,
    onUpdateRankingName: (String) -> Unit,
    onUpdateRankingPassword: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onCreateRanking: () -> Unit,
    onUpdateSearchedId: (String) -> Unit,
    onSearchRankingClick: () -> Unit
) {
    val tabs = listOf(TabsDestinations.CREATE_RANKING, TabsDestinations.SEARCH_RANKING)
    val tabIndex = remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
    ) {
        SecondaryTabRow(
            selectedTabIndex = tabIndex.intValue,
            tabs = {
                tabs.forEachIndexed { index, tab ->
                    Tab(text = {
                        Text(stringResource(tab.label)) },
                        selected = tabIndex.intValue == index,
                        onClick = {
                            tabIndex.intValue = index
                        }
                    )
                }
            }
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (tabs[tabIndex.intValue]) {
                TabsDestinations.CREATE_RANKING -> CreateRankingContent(
                    rankingName = { createRankingUiState.rankingName },
                    rankingPassword = createRankingUiState.rankingPassword,
                    rankingNameError = createRankingUiState.rankingNameError,
                    rankingPasswordError = createRankingUiState.rankingPasswordError,
                    passwordVisible = createRankingUiState.isPasswordVisible,
                    onRankingNameChange = onUpdateRankingName,
                    onRankingPasswordChange = onUpdateRankingPassword,
                    onTogglePasswordVisibility = onTogglePasswordVisibility,
                    onCreateClick = onCreateRanking
                )
                TabsDestinations.SEARCH_RANKING -> SearchRankingContent(
                    rankingId = searchRankingUiState.rankingId,
                    rankingIdError = searchRankingUiState.rankingIdError,
                    onRankingIdChange = onUpdateSearchedId,
                    onSearchRankingClick = onSearchRankingClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeContentPreview() {
    HomeContent(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.surface),
        createRankingUiState = CreateRankingUiState(),
        searchRankingUiState = SearchRankingUiState(),
        onUpdateRankingName = {},
        onUpdateRankingPassword = {},
        onTogglePasswordVisibility = {},
        onCreateRanking = {},
        onUpdateSearchedId = {},
        onSearchRankingClick = {}
    )
}