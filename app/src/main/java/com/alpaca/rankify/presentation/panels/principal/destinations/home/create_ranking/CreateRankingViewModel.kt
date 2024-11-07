package com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpaca.rankify.domain.use_cases.UseCases
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.CreateRanking
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.HideLoading
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.HideRankingNameError
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.HideRankingPasswordError
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.RequestIdle
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.ShowLoading
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.ShowRankingNameError
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.ShowRankingPasswordError
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.TogglePasswordVisibility
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.UpdateRankingName
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingEvent.UpdateRankingPassword
import com.alpaca.rankify.util.Constants.MIN_RANKING_PASSWORD_SIZE
import com.alpaca.rankify.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRankingViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {
    private val _rankingNameUiState = MutableStateFlow(RankingNameUiState())
    val rankingNameUiState = _rankingNameUiState.asStateFlow()
    private val _rankingPasswordUiState = MutableStateFlow(RankingPasswordUiState())
    val rankingPasswordUiState = _rankingPasswordUiState.asStateFlow()
    private val _rankingRequestState = MutableStateFlow<RequestState<Long>>(RequestState.Idle)
    val rankingRequestState = _rankingRequestState.asStateFlow()

    fun onEvent(event: CreateRankingEvent) {
        when (event) {
            HideLoading -> hideLoading()
            HideRankingNameError -> hideRankingNameError()
            HideRankingPasswordError -> hideRankingPasswordError()
            ShowLoading -> showLoading()
            ShowRankingNameError -> showRankingNameError()
            ShowRankingPasswordError -> showRankingPasswordError()
            TogglePasswordVisibility -> togglePasswordVisibility()
            RequestIdle -> changeStateToIdle()
            is CreateRanking -> createRanking(name = event.name)
            is UpdateRankingName -> updateRankingName(name = event.name)
            is UpdateRankingPassword -> updateRankingPassword(password = event.password)
        }
    }

    private fun createRanking(
        name: String
    ) {
        val password = rankingPasswordUiState.value.value
        validateRankingName(name = name)
        validateRankingPassword(password = password)
        if (rankingPasswordUiState.value.error || rankingPasswordUiState.value.error) {
            return
        }
        viewModelScope.launch {
            _rankingRequestState.value = RequestState.Loading
            _rankingRequestState.value = async(Dispatchers.IO) {
                useCases.createRanking(
                    rankingName = name,
                    rankingAdminPassword = password
                )
            }.await()
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
        if (password.length < MIN_RANKING_PASSWORD_SIZE) {
            showRankingPasswordError()
        } else {
            hideRankingPasswordError()
        }
    }

    private fun showLoading() {
//        _uiState.update { state ->
//            state.copy(
//                isLoading = true
//            )
//        }
    }

    private fun hideLoading() {
//        _uiState.update { state ->
//            state.copy(
//                isLoading = false
//            )
//        }
    }

    private fun showRankingNameError() {
        _rankingNameUiState.update { state ->
            state.copy(error = true)
        }
    }

    private fun hideRankingNameError() {
        _rankingNameUiState.update { state ->
            state.copy(error = false)
        }
    }

    private fun showRankingPasswordError() {
        _rankingPasswordUiState.update { state ->
            state.copy(error = true)
        }
    }

    private fun hideRankingPasswordError() {
        _rankingPasswordUiState.update { state ->
            state.copy(error = false)
        }
    }

    private fun updateRankingName(name: String) {
        _rankingNameUiState.update { state ->
            state.copy(
                value = name,
                error = if (state.error && name.isNotEmpty()) false else state.error
            )

        }
    }

    private fun updateRankingPassword(password: String) {
        _rankingPasswordUiState.update { state ->
            state.copy(
                value = password,
                error = if (state.error && password.length >= 6) false else state.error
            )
        }
    }

    private fun togglePasswordVisibility() {
        _rankingPasswordUiState.update { state ->
            state.copy(
                isVisible = state.isVisible.not()
            )
        }
    }

    private fun changeStateToIdle() {
        _rankingRequestState.value = RequestState.Idle
    }
}