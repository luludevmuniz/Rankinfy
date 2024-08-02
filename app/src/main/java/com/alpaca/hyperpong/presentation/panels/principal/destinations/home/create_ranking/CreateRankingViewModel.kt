package com.alpaca.hyperpong.presentation.panels.principal.destinations.home.create_ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpaca.hyperpong.domain.use_cases.UseCases
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.CreateRanking
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.HideLoading
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.HideRankingNameError
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.HideRankingPasswordError
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.RankingCreated
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.ShowLoading
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.ShowRankingNameError
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.ShowRankingPasswordError
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.TogglePasswordVisibility
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.UpdateRankingName
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.UpdateRankingPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRankingViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateRankingUiState())
    val uiState = _uiState.asStateFlow()
    private val _navigationEvent = MutableSharedFlow<RankingCreated>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onEvent(event: CreateRankingEvent) {
        when (event) {
            HideLoading -> hideLoading()
            HideRankingNameError -> hideRankingNameError()
            HideRankingPasswordError -> hideRankingPasswordError()
            ShowLoading -> showLoading()
            ShowRankingNameError -> showRankingNameError()
            ShowRankingPasswordError -> showRankingPasswordError()
            TogglePasswordVisibility -> togglePasswordVisibility()
            is CreateRanking -> createRanking(name = event.name)
            is UpdateRankingName -> updateRankingName(name = event.name)
            is UpdateRankingPassword -> updateRankingPassword(password = event.password)
        }
    }

    private fun createRanking(
        name: String
    ) {
        val password = uiState.value.rankingPassword
        validateRankingName(name = name)
        validateRankingPassword(password = password)
        if (uiState.value.rankingNameError || uiState.value.rankingPasswordError) {
            return
        }
        viewModelScope.launch {
            try {
                val id = useCases.createRanking(rankingName = name)
                _navigationEvent.emit(
                    RankingCreated(
                        id = id,
                        adminPassword = password
                    )
                )
                resetUiState()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun validateRankingName(name: String) {
        if (name.isEmpty()) {
            showRankingNameError()
        } else {
            hideRankingNameError()
        }
    }

    private fun validateRankingPassword(password: String) {
        if (password.length < 6) {
            showRankingPasswordError()
        } else {
            hideRankingPasswordError()
        }
    }

    private fun showLoading() {
        _uiState.update { state ->
            state.copy(
                isLoading = true
            )
        }
    }

    private fun hideLoading() {
        _uiState.update { state ->
            state.copy(
                isLoading = false
            )
        }
    }

    private fun showRankingNameError() {
        _uiState.update { state ->
            state.copy(
                rankingNameError = true
            )
        }
    }

    private fun hideRankingNameError() {
        _uiState.update { state ->
            state.copy(
                rankingNameError = false
            )
        }
    }

    private fun showRankingPasswordError() {
        _uiState.update { state ->
            state.copy(
                rankingPasswordError = true
            )
        }
    }

    private fun hideRankingPasswordError() {
        _uiState.update { state ->
            state.copy(
                rankingPasswordError = false
            )
        }
    }

    private fun updateRankingName(name: String) {
        _uiState.update { state ->
            state.copy(
                rankingName = name,
                rankingNameError = if (state.rankingNameError && name.isNotEmpty()) false
                else state.rankingNameError
            )
        }
    }

    private fun updateRankingPassword(password: String) {
        _uiState.update { state ->
            state.copy(
                rankingPassword = password,
                rankingPasswordError = if (state.rankingPasswordError && password.length >= 6) false
                else state.rankingPasswordError
            )
        }
    }

    private fun togglePasswordVisibility() {
        _uiState.update { state ->
            state.copy(
                isPasswordVisible = !state.isPasswordVisible
            )
        }
    }

    private fun resetUiState() {
        _uiState.update { state ->
            state.copy(
                isLoading = false,
                rankingName = "",
                rankingPassword = "",
            )
        }
    }
}