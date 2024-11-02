package com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpaca.rankify.domain.use_cases.UseCases
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.HideLoading
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.HideSearchedIdError
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.RankingSearched
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.SearchRanking
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.ShowLoading
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.ShowSearchedIdError
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.UpdateSearchedId
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val _navigationEvent = Channel<RankingSearched>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onEvent(event: SearchRankingEvent) {
        when (event) {
            HideLoading -> hideLoading()
            HideSearchedIdError -> hideSearchedIdError()
            SearchRanking -> searchRanking()
            ShowLoading -> showLoading()
            ShowSearchedIdError -> showSearchedIdError()
            is UpdateSearchedId -> updateSearchedId(id = event.id)
        }
    }

    private fun searchRanking() {
        val id = uiState.value.rankingId
        validateRankingId(id = id)
        if (uiState.value.rankingIdError) {
            return
        }
        viewModelScope.launch {
            val searchedRankingId = useCases.searchRank(id = id.toLongOrNull() ?: 0L)
            _navigationEvent.send(RankingSearched(id = searchedRankingId))
            resetUiState()
        }
    }

    private fun validateRankingId(id: String) {
        if (id.isEmpty()) {
            showSearchedIdError()
        } else {
            hideSearchedIdError()
        }
    }

    private fun updateSearchedId(id: String) {
        _uiState.update { state ->
            state.copy(
                rankingId = id,
                rankingIdError =
                if (state.rankingIdError && id.isNotEmpty()) {
                    false
                } else {
                    state.rankingIdError
                },
            )
        }
    }

    private fun showSearchedIdError() {
        _uiState.update { state ->
            state.copy(
                rankingIdError = true,
            )
        }
    }

    private fun hideSearchedIdError() {
        _uiState.update { state ->
            state.copy(
                rankingIdError = false,
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

    private fun resetUiState() {
        _uiState.update { state ->
            state.copy(
                isLoading = false,
                rankingId = "",
            )
        }
    }
}
