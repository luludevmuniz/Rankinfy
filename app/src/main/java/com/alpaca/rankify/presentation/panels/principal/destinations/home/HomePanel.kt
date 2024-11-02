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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.CreateRanking
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.TogglePasswordVisibility
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.UpdateRankingName
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.UpdateRankingPassword
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingViewModel
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.SearchRanking
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.UpdateSearchedId
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navigateToRanking: (Long, String?) -> Unit,
    createRankingViewModel: CreateRankingViewModel = hiltViewModel(),
    searchRankingViewModel: SearchRankingViewModel = hiltViewModel()
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val rankingNameUiState by createRankingViewModel.rankingNameUiState.collectAsStateWithLifecycle()
    val rankingPasswordUiState by createRankingViewModel.rankingPasswordUiState.collectAsStateWithLifecycle()
    val searchRankingUiState by searchRankingViewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = rememberUpdatedState(newValue = LocalLifecycleOwner.current)
    LaunchedEffect(lifecycleOwner.value.lifecycle) {
        lifecycleOwner.value.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                createRankingViewModel.navigationEvent.collect {
                    navigateToRanking(
                        it.id,
                        it.adminPassword
                    )
                }
            }
            launch {
                searchRankingViewModel.navigationEvent.collect {
                    navigateToRanking(
                        it.id,
                        null
                    )
                }
            }
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
            nameState = { rankingNameUiState },
            passwordState = { rankingPasswordUiState },
            searchRankingUiState = { searchRankingUiState },
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
                createRankingViewModel.onEvent(CreateRanking(name = rankingNameUiState.value))
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