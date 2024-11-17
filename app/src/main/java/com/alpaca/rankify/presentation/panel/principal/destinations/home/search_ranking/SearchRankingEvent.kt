package com.alpaca.rankify.presentation.panel.principal.destinations.home.search_ranking

sealed interface SearchRankingEvent {
    data object ShowSearchedIdError: SearchRankingEvent
    data object HideSearchedIdError: SearchRankingEvent
    data object ShowLoading: SearchRankingEvent
    data object HideLoading: SearchRankingEvent
    data object SearchRanking: SearchRankingEvent
    data object ToggleAdminPasswordVisibility: SearchRankingEvent
    data object ChangeIsAdministrator: SearchRankingEvent
    data object RequestIdle: SearchRankingEvent
    data class UpdateSearchedId(val id: String): SearchRankingEvent
    data class UpdateRankingAdminPassword(val password: String): SearchRankingEvent
}