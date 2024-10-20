package com.alpaca.rankify.presentation.panels.principal.destinations.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.CreateRanking
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.TogglePasswordVisibility
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.UpdateRankingName
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.UpdateRankingPassword
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingViewModel
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.SearchRanking
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.UpdateSearchedId
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.UpdateSearchedName
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingViewModel

@Composable
fun HomeScreen(
    navigateToRanking: (Long, String?) -> Unit,
    createRankingViewModel: CreateRankingViewModel = hiltViewModel(),
    searchRankingViewModel: SearchRankingViewModel = hiltViewModel()
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val createRankingUiState by createRankingViewModel.uiState.collectAsStateWithLifecycle()
    val searchRankingUiState by searchRankingViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        createRankingViewModel.navigationEvent.collect {
            navigateToRanking(
                it.id,
                it.adminPassword
            )
        }
    }

    LaunchedEffect(Unit) {
        searchRankingViewModel.navigationEvent.collect {
            navigateToRanking(
                it.id,
                null
            )
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        HomeContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            createRankingUiState = createRankingUiState,
            searchRankingUiState = searchRankingUiState,
            onUpdateRankingName = { name ->
                createRankingViewModel.onEvent(UpdateRankingName(name = name))
            },
            onUpdateRankingPassword = { password ->
                createRankingViewModel.onEvent(UpdateRankingPassword(password = password))
            },
            onTogglePasswordVisibility = {
                createRankingViewModel.onEvent(TogglePasswordVisibility)
            },
            onCreateRanking = {
                createRankingViewModel.onEvent(CreateRanking(name = createRankingUiState.rankingName))
            },
            onUpdateSearchedName = { name ->
                searchRankingViewModel.onEvent(UpdateSearchedName(name = name))
            },
            onUpdateSearchedId = { id ->
                searchRankingViewModel.onEvent(UpdateSearchedId(id = id))
            },
            onSearchRankingClick = {
                searchRankingViewModel.onEvent(SearchRanking)
            }
        )
    }
}