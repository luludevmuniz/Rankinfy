package com.alpaca.rankify.presentation.panels.ranking_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.alpaca.rankify.domain.model.CreateRankingDTO
import com.alpaca.rankify.domain.model.Player
import com.alpaca.rankify.domain.model.Ranking
import com.alpaca.rankify.domain.model.UpdatePlayerDTO
import com.alpaca.rankify.domain.use_cases.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCases: UseCases
) : ViewModel() {
    private val localRankingId = checkNotNull(savedStateHandle.get<Long>("id"))
    private val adminPassword = savedStateHandle.get<String>("adminPassword")
    private var createRemoteRankingJob: Job? = null
    private var syncRemoteRankingJob: Job? = null
    private val _uiState = MutableStateFlow(RankingDetailsUiState())
    val uiState: StateFlow<RankingDetailsUiState> = _uiState.asStateFlow()
    val ranking: StateFlow<Ranking?> = useCases.getRank(id = localRankingId)
        .catch { e ->
            e.printStackTrace()

            //TODO: Logging
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L, 0),
            initialValue = Ranking()
        )

    init {
        viewModelScope.launch {
            ranking.collectLatest { collectedRanking ->
                collectedRanking?.let {
                    if (collectedRanking.localId != 0L) {
                        if (it.remoteId == null) {
                            createRemoteRanking(it)
                            cancel()
                        } else {
                            syncRemoteRanking(it)
                            cancel()
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: RankingDetailsEvent) {
        when (event) {
            is RankingDetailsEvent.CreatePlayer -> createPlayer(player = event.player)
            is RankingDetailsEvent.DeleteRanking -> deleteRanking(
                localId = event.localId,
                remoteId = event.remoteId
            )

            is RankingDetailsEvent.UpdatePlayer -> updatePlayer(player = event.player)
            is RankingDetailsEvent.OnNewPlayerNameChange -> changeNewPlayerName(name = event.name)
            is RankingDetailsEvent.OnNewPlayerScoreChange -> changeNewPlayerScore(score = event.score)
            is RankingDetailsEvent.DeletePlayer -> deletePlayer(event.player)
            is RankingDetailsEvent.ShowEditPlayerDialog -> showEditPlayerDialog(player = event.player)
            is RankingDetailsEvent.OnSelectedPlayerNameChange -> changeSelectedPlayerName(name = event.name)
            is RankingDetailsEvent.OnSelectedPlayerScoreChange -> changeSelectedPlayerScore(score = event.score)
            is RankingDetailsEvent.ShowDeletePlayerDialog -> showDeletePlayerDialog(player = event.player)
            RankingDetailsEvent.HideDeletePlayerDialog -> hideDeletePlayerDialog()
            RankingDetailsEvent.HideEditPlayerDialog -> hideEditPlayerDialog()
            RankingDetailsEvent.HideCreatePlayerDialog -> hideCreatePlayerDialog()
            RankingDetailsEvent.HideDeleteRankingDialog -> hideDeleteRankingDialog()
            RankingDetailsEvent.ShowCreatePlayerDialog -> showCreatePlayerDialog()
            RankingDetailsEvent.ShowDeleteRankingDialog -> showDeleteRankingDialog()
            RankingDetailsEvent.HidePlayerDialogNameFieldError -> hidePlayerDialogNameFieldError()
            RankingDetailsEvent.HidePlayerDialogScoreFieldError -> hidePlayerDialogScoreFieldError()
            RankingDetailsEvent.ShowPlayerDialogNameFieldError -> showPlayerDialogNameFieldError()
            RankingDetailsEvent.ShowPlayerDialogScoreFieldError -> showPlayerDialogScoreFieldError()
        }
    }

    private fun syncRemoteRanking(ranking: Ranking) {
        syncRemoteRankingJob = viewModelScope.launch(Dispatchers.IO) {
            useCases.scheduleSyncRank(ranking = ranking)
                .collectLatest { workInfo ->
                    if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
                        _uiState.update { state ->
                            state.copy(isSyncing = false)
                        }
                        cancel()
                    }
                }
        }
    }

    private fun createRemoteRanking(ranking: Ranking) {
        createRemoteRankingJob = viewModelScope.launch(Dispatchers.IO) {
            useCases.scheduleRemoteRankCreation(
                ranking = CreateRankingDTO(
                    name = ranking.name,
                    adminPassword = adminPassword.orEmpty(),
                    mobileId = ranking.localId
                )
            ).collectLatest { workInfo ->
                if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
                    _uiState.update { state ->
                        state.copy(isSyncing = false)
                    }
                    cancel()
                }
            }
        }
    }

    private fun deleteRanking(
        localId: Long,
        remoteId: Long?
    ) {
        hideDeleteRankingDialog()
        viewModelScope.launch(Dispatchers.IO) {
            val success = async {
                useCases.deleteRank(id = localId) != 0
            }.await()
            if (success && remoteId != null) {
                useCases.scheduleRemoteRankDeletion(rankId = remoteId)
            }
        }
    }

    private fun createPlayer(player: Player) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = async {
                useCases.createPlayer(
                    player = player
                )
            }
            ranking.value?.let { ranking ->
                if (ranking.remoteId != null) {
                    useCases.scheduleRemotePlayerCreation(
                        playerId = id.await(),
                        ranking = ranking
                    )
                }
            }
        }
    }

    private fun updatePlayer(player: Player) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.updatePlayer(
                player = player
            )
            ranking.value?.let { ranking ->
                player.remoteId?.let { remotePlayerId ->
                    useCases.scheduleRemotePlayerUpdateUseCase(
                        player = UpdatePlayerDTO(
                            id = remotePlayerId,
                            name = player.name,
                            score = player.score
                        ),
                        ranking = ranking
                    )
                }
            }
        }
    }

    private fun deletePlayer(player: Player) {
        viewModelScope.launch {
            useCases.deletePlayer(player = player)
            ranking.value?.let { ranking ->
                player.remoteId?.let { remotePlayerId ->
                    useCases.scheduleRemotePlayerDeletion(
                        playerId = remotePlayerId,
                        ranking = ranking
                    )
                }
            }
        }
    }

    private fun showEditPlayerDialog(player: Player) {
        _uiState.update { state ->
            state.copy(
                showEditPlayerDialog = true,
                selectedPlayer = player
            )
        }
    }

    private fun hideEditPlayerDialog() {
        _uiState.update { state ->
            state.copy(
                showEditPlayerDialog = false,
                isPlayerDialogNameFieldWithError = false,
                isPlayerDialogScoreFieldWithError = false,
                selectedPlayer = Player()
            )
        }
    }

    private fun changeNewPlayerName(name: String) {
        _uiState.update { state ->
            state.copy(newPlayer = state.newPlayer.copy(name = name))
        }
    }

    private fun changeNewPlayerScore(score: String) {
        _uiState.update { state ->
            state.copy(newPlayer = state.newPlayer.copy(score = score))
        }
    }

    private fun changeSelectedPlayerName(name: String) {
        _uiState.update { state ->
            state.copy(selectedPlayer = state.selectedPlayer.copy(name = name))
        }
    }

    private fun changeSelectedPlayerScore(score: String) {
        _uiState.update { state ->
            state.copy(selectedPlayer = state.selectedPlayer.copy(score = score))
        }
    }

    private fun showDeleteRankingDialog() {
        _uiState.update { state ->
            state.copy(showDeleteRankingDialog = true)
        }
    }

    private fun hideDeleteRankingDialog() {
        _uiState.update { state ->
            state.copy(showDeleteRankingDialog = false)
        }
    }

    private fun showDeletePlayerDialog(player: Player) {
        _uiState.update { state ->
            state.copy(
                showDeletePlayerDialog = true,
                selectedPlayer = player
            )
        }
    }

    private fun hideDeletePlayerDialog() {
        _uiState.update { state ->
            state.copy(
                showDeletePlayerDialog = false,
                selectedPlayer = Player()
            )
        }
    }

    private fun showCreatePlayerDialog() {
        _uiState.update { state ->
            state.copy(showCreatePlayerDialog = true)
        }
    }

    private fun hideCreatePlayerDialog() {
        _uiState.update { state ->
            state.copy(
                showCreatePlayerDialog = false,
                isPlayerDialogNameFieldWithError = false,
                isPlayerDialogScoreFieldWithError = false,
                newPlayer = Player()
            )
        }
    }

    private fun showPlayerDialogNameFieldError() {
        _uiState.update { state ->
            state.copy(isPlayerDialogNameFieldWithError = true)
        }
    }

    private fun hidePlayerDialogNameFieldError() {
        _uiState.update { state ->
            state.copy(isPlayerDialogNameFieldWithError = false)
        }
    }

    private fun showPlayerDialogScoreFieldError() {
        _uiState.update { state ->
            state.copy(isPlayerDialogScoreFieldWithError = true)
        }
    }

    private fun hidePlayerDialogScoreFieldError() {
        _uiState.update { state ->
            state.copy(isPlayerDialogScoreFieldWithError = false)
        }
    }
}