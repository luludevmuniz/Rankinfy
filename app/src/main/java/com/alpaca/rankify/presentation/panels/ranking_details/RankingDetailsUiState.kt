package com.alpaca.rankify.presentation.panels.ranking_details

import com.alpaca.rankify.domain.model.Player

data class RankingDetailsUiState(
    val isSyncing: Boolean = true,
    val showDeleteRankingDialog: Boolean = false,
    val showCreatePlayerDialog: Boolean = false,
    val showEditPlayerDialog: Boolean = false,
    val showDeletePlayerDialog: Boolean = false,
    val isPlayerDialogNameFieldWithError: Boolean = false,
    val isPlayerDialogScoreFieldWithError: Boolean = false,
    val newPlayer: Player = Player(),
    val selectedPlayer: Player = Player()
)

data class RemoteSyncUiState(
    val isSyncing: Boolean = true,
    val message: String = "",
    val attempts: Int = 0,
    val stopReason: String = "",
)