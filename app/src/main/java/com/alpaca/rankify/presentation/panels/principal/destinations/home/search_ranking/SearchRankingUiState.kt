package com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking

data class SearchRankingUiState(
    val isLoading: Boolean = false,
    val rankingId: String = "",
    val rankingIdError: Boolean = false
)
