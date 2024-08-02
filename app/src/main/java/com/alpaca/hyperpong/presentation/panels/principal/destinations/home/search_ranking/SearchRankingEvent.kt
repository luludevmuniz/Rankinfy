package com.alpaca.hyperpong.presentation.panels.principal.destinations.home.search_ranking

sealed interface SearchRankingEvent {
    data class UpdateSearchedName(val name: String): SearchRankingEvent
    data class UpdateSearchedId(val id: String): SearchRankingEvent
    data object ShowSearchedNameError: SearchRankingEvent
    data object HideSearchedNameError: SearchRankingEvent
    data object ShowSearchedIdError: SearchRankingEvent
    data object HideSearchedIdError: SearchRankingEvent
    data object ShowLoading: SearchRankingEvent
    data object HideLoading: SearchRankingEvent
    data object SearchRanking: SearchRankingEvent
    data class RankingSearched(val id: Long)
}