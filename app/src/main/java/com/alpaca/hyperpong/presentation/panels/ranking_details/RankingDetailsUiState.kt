package com.alpaca.hyperpong.presentation.panels.ranking_details

import com.alpaca.hyperpong.domain.model.Player

data class RankingDetailsUiState(
    val isSyncing: Boolean = true,
    val showDeleteRankingDialog: Boolean = false,
    val showCreatePlayerDialog: Boolean = false,
    val showEditPlayerDialog: Boolean = false,
    val isPlayerDialogNameFieldWithError: Boolean = false,
    val isPlayerDialogScoreFieldWithError: Boolean = false,
    val newPlayer: Player = Player(),
    val selectedPlayer: Player = Player()
)