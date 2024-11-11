package com.alpaca.rankify.presentation.panels.ranking_details

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.alpaca.rankify.R
import com.alpaca.rankify.domain.model.Player
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.CreatePlayer
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.DeletePlayer
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.DeleteRanking
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
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.ShowEditPlayerDialog
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.ShowPlayerDialogNameFieldError
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.ShowPlayerDialogScoreFieldError
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsEvent.UpdatePlayer
import com.alpaca.rankify.presentation.panels.ranking_details.component.DeleteDialog
import com.alpaca.rankify.presentation.panels.ranking_details.component.PlayerDialog
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@NonRestartableComposable
@Composable
fun RankingDetailsScreen(
    modifier: Modifier = Modifier,
    rankingDetailsViewModel: RankingDetailsViewModel = hiltViewModel(),
    rankingId: Long? = null,
    onBackClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        rankingId?.let { id ->
            rankingDetailsViewModel.setLocalRankingId(id)
        }
    }
    val ranking by rankingDetailsViewModel.ranking.collectAsStateWithLifecycle()
    val uiState by rankingDetailsViewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = rememberUpdatedState(newValue = LocalLifecycleOwner.current)

    LaunchedEffect(lifecycleOwner.value.lifecycle) {
        lifecycleOwner.value.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                rankingDetailsViewModel.navigationEvent.collect {
                    onBackClick()
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
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
                        onClick = remember {
                            {
                                rankingDetailsViewModel.onEvent(ShowDeleteRankingDialog)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.deletar_raking)
                        )
                    }
//                    IconButton(
//                        onClick = {
//
//                        },
//                        enabled = ranking?.remoteId != null
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.Share,
//                            contentDescription = stringResource(R.string.compartilhar)
//                        )
//                    }
                }
            )
        },
        floatingActionButton = {
            if (ranking?.isAdmin == true) {
                FloatingActionButton(
                    onClick = remember {
                        {
                            rankingDetailsViewModel.onEvent(ShowCreatePlayerDialog)
                        }
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
        ranking?.let {
            RankingDetailsContent(
                modifier = Modifier.padding(paddingValues),
                isSyncing = { uiState.isSyncing },
                isAdmin = { ranking?.isAdmin ?: false },
                lastUpdate = { ranking?.formattedLastUpdated.orEmpty() },
                players = { ranking?.sortedPlayers ?: persistentListOf() },
                onDeletePlayer = remember {
                    { player: Player ->
                        rankingDetailsViewModel.onEvent(
                            ShowDeletePlayerDialog(
                                player
                            )
                        )
                    }
                },
                onEditPlayer = remember {
                    { player: Player ->
                        rankingDetailsViewModel.onEvent(
                            ShowEditPlayerDialog(
                                player
                            )
                        )
                    }
                }
            )
        }

        if (uiState.showDeleteRankingDialog) {
            DeleteDialog(
                title = "Deseja mesmo excluir o ranking?",
                text = "Essa ação não pode ser desfeita.",
                onDismissRequest = remember {
                    {
                        rankingDetailsViewModel.onEvent(HideDeleteRankingDialog)
                    }
                },
                onConfirmClick = remember {
                    {
                        ranking?.let {
                            rankingDetailsViewModel.onEvent(
                                DeleteRanking(
                                    localId = it.localId,
                                    remoteId = it.remoteId,
                                    isAdmin = it.isAdmin
                                )
                            )
                        }
                    }
                }
            )
        }

        if (uiState.showDeletePlayerDialog) {
            DeleteDialog(
                title = "Deseja mesmo excluir o jogador?",
                text = "Essa ação não pode ser desfeita.",
                onDismissRequest = remember {
                    {
                        rankingDetailsViewModel.onEvent(HideDeletePlayerDialog)
                    }
                },
                onConfirmClick = remember {
                    {
                        uiState.selectedPlayer.let {
                            rankingDetailsViewModel.onEvent(DeletePlayer(it))
                            rankingDetailsViewModel.onEvent(HideDeletePlayerDialog)
                        }
                    }
                }
            )
        }

        if (uiState.showEditPlayerDialog) {
            PlayerDialog(
                playerName = { uiState.selectedPlayer.name },
                playerScore = { uiState.selectedPlayer.score },
                isNameWithError = { uiState.isPlayerDialogNameFieldWithError },
                isScoreWithError = { uiState.isPlayerDialogScoreFieldWithError },
                title = "Editar jogador",
                icon = Icons.Default.Edit,
                onPlayerNameChange = remember {
                    { name ->
                        if (uiState.isPlayerDialogNameFieldWithError) {
                            rankingDetailsViewModel.onEvent(HidePlayerDialogNameFieldError)
                        }
                        rankingDetailsViewModel.onEvent(OnSelectedPlayerNameChange(name = name))
                    }
                },
                onPlayerScoreChange = remember {
                    { score ->
                        if (uiState.isPlayerDialogScoreFieldWithError) {
                            rankingDetailsViewModel.onEvent(HidePlayerDialogScoreFieldError)
                        }
                        rankingDetailsViewModel.onEvent(
                            OnSelectedPlayerScoreChange(
                                score = score
                            )
                        )
                    }
                },
                onConfirmClick = remember {
                    {
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
                    }
                },
                onDismissRequest = remember {
                    {
                        rankingDetailsViewModel.onEvent(HideEditPlayerDialog)
                    }
                }
            )
        }

        if (uiState.showCreatePlayerDialog) {
            PlayerDialog(
                playerName = { uiState.newPlayer.name },
                playerScore = { uiState.newPlayer.score },
                isNameWithError = { uiState.isPlayerDialogNameFieldWithError },
                isScoreWithError = { uiState.isPlayerDialogScoreFieldWithError },
                title = "Adicionar jogador",
                icon = Icons.Default.AddCircle,
                onPlayerNameChange = remember {
                    {
                        if (uiState.isPlayerDialogNameFieldWithError) {
                            rankingDetailsViewModel.onEvent(HidePlayerDialogNameFieldError)
                        }
                        rankingDetailsViewModel.onEvent(OnNewPlayerNameChange(name = it))
                    }
                },
                onPlayerScoreChange = remember {
                    {
                        if (uiState.isPlayerDialogScoreFieldWithError) {
                            rankingDetailsViewModel.onEvent(HidePlayerDialogScoreFieldError)
                        }
                        rankingDetailsViewModel.onEvent(OnNewPlayerScoreChange(score = it))
                    }
                },
                onConfirmClick = remember {
                    {
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
                    }
                },
                onDismissRequest = remember {
                    {
                        rankingDetailsViewModel.onEvent(HideCreatePlayerDialog)
                    }
                }
            )
        }
    }
}