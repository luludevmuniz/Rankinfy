package com.alpaca.rankify.presentation.panel.principal.destinations.home.create_ranking

data class RankingNameUiState(
    val value: String = "",
    val error: Boolean = false
)

data class RankingPasswordUiState(
    val value: String = "",
    val error: Boolean = false,
    val isVisible: Boolean = false
)