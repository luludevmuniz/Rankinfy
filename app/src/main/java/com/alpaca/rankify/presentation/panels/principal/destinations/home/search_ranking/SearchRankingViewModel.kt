package com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpaca.rankify.domain.use_cases.UseCases
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.RankingNameUiState
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.RankingPasswordUiState
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.HideLoading
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.HideSearchedIdError
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.RankingSearched
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.SearchRanking
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.ShowLoading
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.ShowSearchedIdError
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.UpdateRankingAdminPassword
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.UpdateSearchedId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchRankingViewModel @Inject constructor(private val useCases: UseCases) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchRankingUiState())
    val uiState = _uiState.asStateFlow()
    private val _rankingIdUiState = MutableStateFlow(RankingNameUiState())
    val rankingIdUiState = _rankingIdUiState.asStateFlow()
    private val _rankingAdminPasswordUiState = MutableStateFlow(RankingPasswordUiState())
    val rankingAdminPasswordUiState = _rankingAdminPasswordUiState.asStateFlow()
    private val _navigationEvent = Channel<RankingSearched>()
    val navigationEvent = _navigationEvent.receiveAsFlow()
    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }
    fun onEvent(event: SearchRankingEvent) {
        when (event) {
            HideLoading -> hideLoading()
            HideSearchedIdError -> hideSearchedIdError()
            SearchRanking -> searchRanking()
            ShowLoading -> showLoading()
            ShowSearchedIdError -> showSearchedIdError()
            SearchRankingEvent.ChangeIsAdministrator -> changeIsAdministrator()
            SearchRankingEvent.ToggleAdminPasswordVisibility -> changeAdminPasswordVisibility()
            is UpdateSearchedId -> updateSearchedId(id = event.id)
            is UpdateRankingAdminPassword -> updateRankingAdminPassword(password = event.password)
        }
    }

    private fun searchRanking() {
        val id = _rankingIdUiState.value.value
        val password = _rankingAdminPasswordUiState.value.value
        validateRankingId(id = id)
        if (_uiState.value.isAdministrator) {
            validadeRankingAdminPassword(password)
        }
        if (
            _rankingIdUiState.value.error ||
            _uiState.value.isAdministrator && _rankingAdminPasswordUiState.value.error
        ) {
            return
        }
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val searchedRankingId = async {
                    useCases.searchRanking(
                        id = id.toLongOrNull() ?: 0L,
                        adminPassword = password
                    )
                }
                val alreadyExistingRanking = useCases.getRankingWithRemoteId(searchedRankingId)
                if (useCases.getRankingWithRemoteId(searchedRankingId) != null) {

                }
                _navigationEvent.send(RankingSearched(id = searchedRankingId.await()))
                resetUiState()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun validateRankingId(id: String) {
        if (id.isEmpty()) {
            showSearchedIdError()
        } else {
            hideSearchedIdError()
        }
    }

    private fun validadeRankingAdminPassword(password: String) {
        if (password.isEmpty()) {
            showSearchedAdminPasswordError()
        } else {
            hideSearchedAdminPasswordError()
        }
    }

    private fun updateSearchedId(id: String) {
        _rankingIdUiState.update { state ->
            state.copy(
                value = id,
                error =
                if (state.error && id.isNotEmpty()) {
                    false
                } else {
                    state.error
                },
            )
        }
    }

    private fun showSearchedIdError() {
        _rankingIdUiState.update { state ->
            state.copy(
                error = true,
            )
        }
    }

    private fun hideSearchedIdError() {
        _rankingIdUiState.update { state ->
            state.copy(
                error = false,
            )
        }
    }

    private fun showLoading() {
        _uiState.update { state ->
            state.copy(
                isLoading = true,
            )
        }
    }

    private fun hideLoading() {
        _uiState.update { state ->
            state.copy(
                isLoading = false,
            )
        }
    }

    private fun updateRankingAdminPassword(password: String) {
        _rankingAdminPasswordUiState.update { state ->
            state.copy(
                value = password,
                error =
                if (state.error && password.isNotEmpty()) {
                    false
                } else {
                    state.error
                },
            )
        }
    }

    private fun showSearchedAdminPasswordError() {
        _rankingAdminPasswordUiState.update { state ->
            state.copy(
                error = true,
            )
        }
    }

    private fun hideSearchedAdminPasswordError() {
        _rankingAdminPasswordUiState.update { state ->
            state.copy(
                error = false,
            )
        }
    }

    private fun changeAdminPasswordVisibility() {
        _rankingAdminPasswordUiState.update { state ->
            state.copy(
                isVisible = !state.isVisible
            )
        }
    }

    private fun changeIsAdministrator() {
        _uiState.update { state ->
            state.copy(
                isAdministrator = !state.isAdministrator
            )
        }
    }

    private fun resetUiState() {
        _uiState.update { state ->
            state.copy(
                isLoading = false,
            )
        }
        _rankingIdUiState.update { state ->
            state.copy(
                value = "",
                error = false,
            )
        }
        _rankingAdminPasswordUiState.update { state ->
            state.copy(
                value = "",
                error = false,
                isVisible = false
            )
        }
    }
}
