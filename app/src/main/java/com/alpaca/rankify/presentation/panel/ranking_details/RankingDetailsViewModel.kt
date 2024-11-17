package com.alpaca.rankify.presentation.panel.ranking_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo.State.BLOCKED
import androidx.work.WorkInfo.State.CANCELLED
import androidx.work.WorkInfo.State.ENQUEUED
import androidx.work.WorkInfo.State.FAILED
import androidx.work.WorkInfo.State.RUNNING
import androidx.work.WorkInfo.State.SUCCEEDED
import com.alpaca.rankify.domain.model.CreateRankingDTO
import com.alpaca.rankify.domain.model.Player
import com.alpaca.rankify.domain.model.Ranking
import com.alpaca.rankify.domain.model.UpdatePlayerDTO
import com.alpaca.rankify.domain.use_cases.UseCases
import com.alpaca.rankify.navigation.RankingDestinationArgs
import com.alpaca.rankify.presentation.panel.ranking_details.RankingDetailsEvent.OnRankingDeleted
import com.alpaca.rankify.util.constant.WorkManagerConstants.WorkData.MESSAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCases: UseCases
) : ViewModel() {
    private val rankingArgs = MutableStateFlow(
        savedStateHandle.get<Long>("id")?.let { id ->
            RankingDestinationArgs(
                id,
                savedStateHandle.get<String>("adminPassword")
            )
        }
    )
    private val _uiState = MutableStateFlow(RankingDetailsUiState())
    val uiState: StateFlow<RankingDetailsUiState> = _uiState.asStateFlow()
    private val _remoteSyncUiState = MutableStateFlow(RemoteSyncUiState())
    val remoteSyncUiState = _remoteSyncUiState.asStateFlow()

    private val _navigationEvent = Channel<OnRankingDeleted>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val ranking: StateFlow<Ranking?> = rankingArgs
        .map { it }
        .distinctUntilChanged()
        .filterNotNull()
        .flatMapLatest { args ->
            useCases.getRanking(args.id)
                .catch { e -> e.printStackTrace() }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Ranking())

    fun setLocalRankingArgs(rankingArgs: RankingDestinationArgs) {
        this.rankingArgs.value = rankingArgs
    }

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
                isAdmin = event.isAdmin,
                remoteId = event.remoteId
            )

            OnRankingDeleted -> closeRankingScreen()
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
        viewModelScope.launch(Dispatchers.IO) {
            useCases.scheduleSyncRanking(ranking = ranking)
                .collectLatest { workInfo ->
                    when (workInfo?.state) {
                        ENQUEUED -> {
                            _remoteSyncUiState.update { state ->
                                state.copy(
                                    isSyncing = false,
                                    message = "",
                                    attempts = workInfo.runAttemptCount,
                                    state = "Enqueued"
                                )
                            }
                        }

                        RUNNING -> {
                            _remoteSyncUiState.update { state ->
                                state.copy(
                                    isSyncing = true,
                                    message = "",
                                    attempts = workInfo.runAttemptCount,
                                    state = "Running"
                                )
                            }
                        }

                        SUCCEEDED -> {
                            _remoteSyncUiState.update { state ->
                                state.copy(
                                    isSyncing = false,
                                    message = workInfo.outputData.getString(MESSAGE),
                                    attempts = workInfo.runAttemptCount,
                                    state = "Succeeded"
                                )
                            }
                            cancel()
                        }

                        FAILED -> {
                            _remoteSyncUiState.update { state ->
                                state.copy(
                                    isSyncing = false,
                                    message = workInfo.outputData.getString(MESSAGE),
                                    attempts = workInfo.runAttemptCount,
                                    state = "Failed"
                                )
                            }
                            cancel()
                        }

                        BLOCKED -> {
                            _remoteSyncUiState.update { state ->
                                state.copy(
                                    isSyncing = false,
                                    message = workInfo.outputData.getString(MESSAGE),
                                    attempts = workInfo.runAttemptCount,
                                    state = "Blocked"
                                )
                            }
                        }

                        CANCELLED -> {
                            _remoteSyncUiState.update { state ->
                                state.copy(
                                    isSyncing = false,
                                    message = workInfo.outputData.getString(MESSAGE),
                                    attempts = workInfo.runAttemptCount,
                                    state = "Cancelled"
                                )
                            }
                            cancel()
                        }

                        null -> {
                            _remoteSyncUiState.update { state ->
                                state.copy(
                                    isSyncing = false,
                                    message = "",
                                    attempts = 0,
                                    state = ""
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun createRemoteRanking(ranking: Ranking) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.scheduleRemoteRankingCreation(
                ranking = CreateRankingDTO(
                    name = ranking.name,
                    adminPassword = rankingArgs.value?.adminPassword.orEmpty(),
                    mobileId = ranking.localId
                )
            ).collectLatest { workInfo ->
                when (workInfo?.state) {
                    ENQUEUED -> {
                        _remoteSyncUiState.update { state ->
                            state.copy(
                                isSyncing = false,
                                message = "",
                                attempts = workInfo.runAttemptCount,
                                state = "Enqueued"
                            )
                        }
                    }

                    RUNNING -> {
                        _remoteSyncUiState.update { state ->
                            state.copy(
                                isSyncing = true,
                                message = "",
                                attempts = workInfo.runAttemptCount,
                                state = "Running"
                            )
                        }
                    }

                    SUCCEEDED -> {
                        _remoteSyncUiState.update { state ->
                            state.copy(
                                isSyncing = false,
                                message = workInfo.outputData.getString(MESSAGE),
                                attempts = workInfo.runAttemptCount,
                                state = "Succeeded"
                            )
                        }
                        cancel()
                    }

                    FAILED -> {
                        _remoteSyncUiState.update { state ->
                            state.copy(
                                isSyncing = false,
                                message = workInfo.outputData.getString(MESSAGE),
                                attempts = workInfo.runAttemptCount,
                                state = "Failed"
                            )
                        }
                        cancel()
                    }

                    BLOCKED -> {
                        _remoteSyncUiState.update { state ->
                            state.copy(
                                isSyncing = false,
                                message = workInfo.outputData.getString(MESSAGE),
                                attempts = workInfo.runAttemptCount,
                                state = "Blocked"
                            )
                        }
                    }

                    CANCELLED -> {
                        _remoteSyncUiState.update { state ->
                            state.copy(
                                isSyncing = false,
                                message = workInfo.outputData.getString(MESSAGE),
                                attempts = workInfo.runAttemptCount,
                                state = "Cancelled"
                            )
                        }
                        cancel()
                    }

                    null -> {
                        _remoteSyncUiState.update { state ->
                            state.copy(
                                isSyncing = false,
                                message = "",
                                attempts = 0,
                                state = ""
                            )
                        }
                    }
                }
            }
        }
    }

    private fun deleteRanking(
        localId: Long,
        remoteId: Long?,
        isAdmin: Boolean
    ) {
        hideDeleteRankingDialog()
        viewModelScope.launch(Dispatchers.IO) {
            val success = async {
                useCases.deleteRanking(id = localId) != 0
            }.await()
            remoteId?.let {
                if (success && isAdmin) {
                    useCases.scheduleRemoteRankingDeletion(rankId = remoteId)
                }
            }
            closeRankingScreen()
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

    private fun closeRankingScreen() {
        viewModelScope.launch {
            _navigationEvent.send(OnRankingDeleted)
        }
    }
}