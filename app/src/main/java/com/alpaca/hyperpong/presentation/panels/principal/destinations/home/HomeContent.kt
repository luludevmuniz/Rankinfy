package com.alpaca.hyperpong.presentation.panels.principal.destinations.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alpaca.hyperpong.navigation.AppDestinations
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.create_ranking.CreateRankingContent
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.create_ranking.CreateRankingUiState
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.search_ranking.SearchRankingContent
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.search_ranking.SearchRankingUiState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
 fun HomeContent(
    modifier: Modifier = Modifier,
    createRankingUiState: CreateRankingUiState,
    searchRankingUiState: SearchRankingUiState,
    onUpdateRankingName: (String) -> Unit,
    onUpdateRankingPassword: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onCreateRanking: () -> Unit,
    onUpdateSearchedName: (String) -> Unit,
    onUpdateSearchedId: (String) -> Unit,
    onSearchRankingClick: () -> Unit
) {
    val tabs = listOf(AppDestinations.HOME, AppDestinations.RANKINGS)
    val tabIndex = remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
    ) {
        SecondaryTabRow(
            selectedTabIndex = tabIndex.intValue,
            tabs = {
                tabs.forEachIndexed { index, tab ->
                    Tab(text = { Text(tab.name) },
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
            when (tabIndex.intValue) {
                0 -> CreateRankingContent(
                    rankingName = createRankingUiState.rankingName,
                    rankingPassword = createRankingUiState.rankingPassword,
                    rankingNameError = createRankingUiState.rankingNameError,
                    rankingPasswordError = createRankingUiState.rankingPasswordError,
                    passwordVisible = createRankingUiState.isPasswordVisible,
                    onRankingNameChange = onUpdateRankingName,
                    onRankingPasswordChange = onUpdateRankingPassword,
                    onTogglePasswordVisibility = onTogglePasswordVisibility,
                    onCreateClick = onCreateRanking
                )
                1 -> {
                    SearchRankingContent(
                        rankingName = searchRankingUiState.rankingName,
                        rankingId = searchRankingUiState.rankingId,
                        rankingNameError = searchRankingUiState.rankingNameError,
                        rankingIdError = searchRankingUiState.rankingIdError,
                        onRankingNameChange = onUpdateSearchedName,
                        onRankingIdChange = onUpdateSearchedId,
                        onSearchRankingClick = onSearchRankingClick
                    )
                }
            }
        }
    }
}