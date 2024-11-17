package com.alpaca.rankify.presentation.panel.principal.destinations.home.create_ranking

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alpaca.rankify.presentation.panel.principal.destinations.home.create_ranking.CreateRankingEvent.CreateRanking
import com.alpaca.rankify.presentation.panel.principal.destinations.home.create_ranking.CreateRankingEvent.TogglePasswordVisibility
import com.alpaca.rankify.presentation.panel.principal.destinations.home.create_ranking.CreateRankingEvent.UpdateRankingName
import com.alpaca.rankify.presentation.panel.principal.destinations.home.create_ranking.CreateRankingEvent.UpdateRankingPassword
import com.alpaca.rankify.util.RequestState

@Composable
fun CreateRankingPanel(
    modifier: Modifier = Modifier,
    viewModel: CreateRankingViewModel = hiltViewModel(),
    navigateToRanking: (Long, String?) -> Unit,
    showSnackBar: (String) -> Unit
) {
    val rankingNameUiState by viewModel.rankingNameUiState.collectAsStateWithLifecycle()
    val rankingPasswordUiState by viewModel.rankingPasswordUiState.collectAsStateWithLifecycle()
    val rankingRequestState by viewModel.rankingRequestState.collectAsStateWithLifecycle()

    LaunchedEffect(rankingRequestState) {
        when (rankingRequestState) {
            is RequestState.Success -> {
                navigateToRanking(
                    rankingRequestState.getSuccessData(),
                    rankingPasswordUiState.value
                )
                viewModel.onEvent(CreateRankingEvent.RequestIdle)
            }

            is RequestState.Error -> {
                showSnackBar(rankingRequestState.getErrorMessage())
                viewModel.onEvent(CreateRankingEvent.RequestIdle)
            }

            RequestState.Loading -> Unit
            RequestState.Idle -> Unit
        }
    }

    CreateRankingContent(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(),
        nameState = { rankingNameUiState },
        passwordState = { rankingPasswordUiState },
        onRankingNameChange = remember {
            { name ->
                viewModel.onEvent(UpdateRankingName(name))
            }
        },
        onRankingPasswordChange = remember {
            { password ->
                viewModel.onEvent(UpdateRankingPassword(password))
            }
        },
        onTogglePasswordVisibility = remember {
            {
                viewModel.onEvent(TogglePasswordVisibility)
            }
        },
        onCreateClick = remember {
            {
                viewModel.onEvent(CreateRanking(name = rankingNameUiState.value))
            }
        },
        isLoading = { rankingRequestState is RequestState.Loading }
    )
}