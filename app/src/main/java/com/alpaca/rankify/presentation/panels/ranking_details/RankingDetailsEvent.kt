package com.alpaca.rankify.presentation.panels.ranking_details

import com.alpaca.rankify.domain.model.Player

sealed interface RankingDetailsEvent {
    data class CreatePlayer(val player: Player) : RankingDetailsEvent
    data class DeleteRanking(
        val localId: Long,
        val isAdmin: Boolean,
        val remoteId: Long?
    ) : RankingDetailsEvent
    data object OnRankingDeleted : RankingDetailsEvent
    data class DeletePlayer(val player: Player) : RankingDetailsEvent
    data class UpdatePlayer(val player: Player) : RankingDetailsEvent
    data class ShowEditPlayerDialog(val player: Player) : RankingDetailsEvent
    data object HideEditPlayerDialog : RankingDetailsEvent
    data class ShowDeletePlayerDialog(val player: Player) : RankingDetailsEvent
    data object HideDeletePlayerDialog : RankingDetailsEvent
    data object ShowDeleteRankingDialog : RankingDetailsEvent
    data object HideDeleteRankingDialog : RankingDetailsEvent
    data object ShowCreatePlayerDialog : RankingDetailsEvent
    data object HideCreatePlayerDialog : RankingDetailsEvent
    data class OnNewPlayerScoreChange(val score: String) : RankingDetailsEvent
    data class OnNewPlayerNameChange(val name: String) : RankingDetailsEvent
    data class OnSelectedPlayerNameChange(val name: String) : RankingDetailsEvent
    data class OnSelectedPlayerScoreChange(val score: String) : RankingDetailsEvent
    data object ShowPlayerDialogNameFieldError : RankingDetailsEvent
    data object HidePlayerDialogNameFieldError : RankingDetailsEvent
    data object ShowPlayerDialogScoreFieldError : RankingDetailsEvent
    data object HidePlayerDialogScoreFieldError : RankingDetailsEvent
}