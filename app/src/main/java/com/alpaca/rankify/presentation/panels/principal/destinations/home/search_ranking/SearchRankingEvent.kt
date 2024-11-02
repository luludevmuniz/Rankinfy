package com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking

sealed interface SearchRankingEvent {
    data class UpdateSearchedId(val id: String): SearchRankingEvent
    data object ShowSearchedIdError: SearchRankingEvent
    data object HideSearchedIdError: SearchRankingEvent
    data object ShowLoading: SearchRankingEvent
    data object HideLoading: SearchRankingEvent
    data object SearchRanking: SearchRankingEvent
    data class RankingSearched(val id: Long)
}