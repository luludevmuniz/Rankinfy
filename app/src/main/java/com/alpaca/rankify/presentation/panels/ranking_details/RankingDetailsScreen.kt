package com.alpaca.rankify.presentation.panels.ranking_details

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alpaca.rankify.R
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.CreatePlayer
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.DeletePlayer
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.DeleteRanking
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.ShowEditPlayerDialog
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.HideCreatePlayerDialog
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.HideDeletePlayerDialog
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.HideDeleteRankingDialog
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.HideEditPlayerDialog
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.HidePlayerDialogNameFieldError
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.HidePlayerDialogScoreFieldError
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.OnNewPlayerNameChange
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.OnNewPlayerScoreChange
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.OnSelectedPlayerNameChange
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.OnSelectedPlayerScoreChange
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.ShowCreatePlayerDialog
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.ShowDeletePlayerDialog
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.ShowDeleteRankingDialog
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.ShowPlayerDialogNameFieldError
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.ShowPlayerDialogScoreFieldError
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.UpdatePlayer
import com.alpaca.rankify.presentation.panels.ranking_details.component.PlayerDialog
import com.alpaca.rankify.presentation.panels.ranking_details.component.DeleteDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingScreen(
    rankingDetailsViewModel: RankingDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()
    val ranking by rankingDetailsViewModel.ranking.collectAsStateWithLifecycle()
    val uiState by rankingDetailsViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(ranking) {
        if (ranking == null) {
            onBackClick()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = ranking?.name.orEmpty())
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.voltar)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            rankingDetailsViewModel.onEvent(ShowDeleteRankingDialog)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.deletar_raking)
                        )
                    }
                    IconButton(
                        onClick = {

                        },
                        enabled = ranking?.remoteId != null
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = stringResource(R.string.compartilhar)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (ranking?.isAdmin == true) {
                FloatingActionButton(
                    onClick = {
                        rankingDetailsViewModel.onEvent(ShowCreatePlayerDialog)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.adicionar_jogador)
                    )
                }
            }
        }
    ) { paddingValues ->
        PullToRefreshBox(
            modifier = Modifier.padding(paddingValues = paddingValues),
            state = state,
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = false
                scope.launch {
                    state.animateToHidden()
                }
            },
        ) {
            ranking?.let {
                RankingContent(
                    uiState = uiState,
                    ranking = it,
                    onDeletePlayer = { player ->
                        rankingDetailsViewModel.onEvent(ShowDeletePlayerDialog(player))
                    },
                    onEditPlayer = { player ->
                        rankingDetailsViewModel.onEvent(ShowEditPlayerDialog(player))
                    }
                )
            }
        }

        if (uiState.showDeleteRankingDialog) {
            DeleteDialog(
                title = "Deseja mesmo excluir o ranking?",
                text = "Essa ação não pode ser desfeita.",
                onDismissRequest = {
                    rankingDetailsViewModel.onEvent(HideDeleteRankingDialog)
                },
                onConfirmClick = {
                    ranking?.let {
                        rankingDetailsViewModel.onEvent(
                            DeleteRanking(
                                name = it.name,
                                localId = it.localId,
                                remoteId = it.remoteId
                            )
                        )
                    }
                }
            )
        }

        if (uiState.showDeletePlayerDialog) {
            DeleteDialog(
                title = "Deseja mesmo excluir o jogador?",
                text = "Essa ação não pode ser desfeita.",
                onDismissRequest = {
                    rankingDetailsViewModel.onEvent(HideDeletePlayerDialog)
                },
                onConfirmClick = {
                    uiState.selectedPlayer.let {
                        rankingDetailsViewModel.onEvent(DeletePlayer(it))
                        rankingDetailsViewModel.onEvent(HideDeletePlayerDialog)
                    }
                }
            )
        }

        if (uiState.showEditPlayerDialog) {
            PlayerDialog(
                playerName = uiState.selectedPlayer.name,
                playerScore = uiState.selectedPlayer.score,
                title = "Editar jogador",
                icon = Icons.Default.Edit,
                isNameWithError = uiState.isPlayerDialogNameFieldWithError,
                isScoreWithError = uiState.isPlayerDialogScoreFieldWithError,
                onPlayerNameChange = { name ->
                    if (uiState.isPlayerDialogNameFieldWithError) {
                        rankingDetailsViewModel.onEvent(HidePlayerDialogNameFieldError)
                    }
                    rankingDetailsViewModel.onEvent(OnSelectedPlayerNameChange(name = name))
                },
                onPlayerScoreChange = { score ->
                    if (uiState.isPlayerDialogScoreFieldWithError) {
                        rankingDetailsViewModel.onEvent(HidePlayerDialogScoreFieldError)
                    }
                    rankingDetailsViewModel.onEvent(
                        OnSelectedPlayerScoreChange(
                            score = score
                        )
                    )
                },
                onConfirmClick = {
                    if (uiState.selectedPlayer.name.isBlank()) {
                        rankingDetailsViewModel.onEvent(ShowPlayerDialogNameFieldError)
                    }
                    if (uiState.selectedPlayer.score.isBlank()) {
                        rankingDetailsViewModel.onEvent(ShowPlayerDialogScoreFieldError)
                    }
                    if (uiState.selectedPlayer.name.isNotBlank() && uiState.selectedPlayer.score.isNotBlank()) {
                        rankingDetailsViewModel.onEvent(HideEditPlayerDialog)
                        rankingDetailsViewModel.onEvent(
                            UpdatePlayer(player = uiState.selectedPlayer)
                        )
                    }
                },
                onDismissRequest = {
                    rankingDetailsViewModel.onEvent(HideEditPlayerDialog)
                }
            )
        }

        if (uiState.showCreatePlayerDialog) {
            PlayerDialog(
                playerName = uiState.newPlayer.name,
                playerScore = uiState.newPlayer.score,
                title = "Adicionar jogador",
                icon = Icons.Default.AddCircle,
                isNameWithError = uiState.isPlayerDialogNameFieldWithError,
                isScoreWithError = uiState.isPlayerDialogScoreFieldWithError,
                onPlayerNameChange = {
                    if (uiState.isPlayerDialogNameFieldWithError) {
                        rankingDetailsViewModel.onEvent(HidePlayerDialogNameFieldError)
                    }
                    rankingDetailsViewModel.onEvent(OnNewPlayerNameChange(name = it))
                },
                onPlayerScoreChange = {
                    if (uiState.isPlayerDialogScoreFieldWithError) {
                        rankingDetailsViewModel.onEvent(HidePlayerDialogScoreFieldError)
                    }
                    rankingDetailsViewModel.onEvent(OnNewPlayerScoreChange(score = it))
                },
                onConfirmClick = {
                    if (uiState.newPlayer.name.isBlank()) {
                        rankingDetailsViewModel.onEvent(ShowPlayerDialogNameFieldError)
                    }
                    if (uiState.newPlayer.score.isBlank()) {
                        rankingDetailsViewModel.onEvent(ShowPlayerDialogScoreFieldError)
                    }
                    if (uiState.newPlayer.name.isNotBlank() && uiState.newPlayer.score.isNotBlank()) {
                        rankingDetailsViewModel.onEvent(HideCreatePlayerDialog)
                        rankingDetailsViewModel.onEvent(
                            CreatePlayer(
                                player = uiState.newPlayer.copy(
                                    rankingId = ranking?.localId ?: 0L
                                )
                            )
                        )
                    }
                },
                onDismissRequest = {
                    rankingDetailsViewModel.onEvent(HideCreatePlayerDialog)
                }
            )
        }
    }
}