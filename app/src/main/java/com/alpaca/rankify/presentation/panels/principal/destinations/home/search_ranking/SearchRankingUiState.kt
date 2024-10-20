package com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking

data class SearchRankingUiState(
    val isLoading: Boolean = false,
    val rankingName: String = "",
    val rankingId: String = "",
    val rankingNameError: Boolean = false,
    val rankingIdError: Boolean = false
)
