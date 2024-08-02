package com.alpaca.hyperpong.presentation.panels.principal.destinations.home.create_ranking

sealed interface CreateRankingEvent {
    data class UpdateRankingName(val name: String): CreateRankingEvent
    data class UpdateRankingPassword(val password: String): CreateRankingEvent
    data object TogglePasswordVisibility: CreateRankingEvent
    data object ShowRankingNameError: CreateRankingEvent
    data object HideRankingNameError: CreateRankingEvent
    data object ShowRankingPasswordError: CreateRankingEvent
    data object HideRankingPasswordError: CreateRankingEvent
    data object ShowLoading: CreateRankingEvent
    data object HideLoading: CreateRankingEvent
    data class CreateRanking(val name: String): CreateRankingEvent
    data class RankingCreated(val id: Long, val adminPassword: String)
}