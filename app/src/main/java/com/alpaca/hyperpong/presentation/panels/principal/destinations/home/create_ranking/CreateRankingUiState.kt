package com.alpaca.hyperpong.presentation.panels.principal.destinations.home.create_ranking

import androidx.compose.runtime.Stable

@Stable
data class CreateRankingUiState(
    val isLoading: Boolean = false,
    val rankingName: String = "",
    val rankingPassword: String = "",
    val rankingNameError: Boolean = false,
    val rankingPasswordError: Boolean = false,
    val isPasswordVisible: Boolean = false
)