package com.alpaca.hyperpong.presentation.panels.principal.destinations.home.search_ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpaca.hyperpong.domain.use_cases.UseCases
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.HideLoading
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.HideSearchedIdError
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.HideSearchedNameError
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.RankingSearched
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.SearchRanking
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.ShowLoading
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.ShowSearchedIdError
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.ShowSearchedNameError
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.UpdateSearchedId
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.UpdateSearchedName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchRankingViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchRankingUiState())
    val uiState = _uiState.asStateFlow()
    private val _navigationEvent = MutableSharedFlow<RankingSearched>()
    val navigationEvent = _navigationEvent.asSharedFlow()


    fun onEvent(event: SearchRankingEvent) {
        when (event) {
            HideLoading -> hideLoading()
            HideSearchedIdError -> hideSearchedIdError()
            HideSearchedNameError -> hideSearchedNameError()
            SearchRanking -> searchRanking()
            ShowLoading -> showLoading()
            ShowSearchedIdError -> showSearchedIdError()
            ShowSearchedNameError -> showSearchedNameError()
            is UpdateSearchedId -> updateSearchedId(id = event.id)
            is UpdateSearchedName -> updateSearchedName(name = event.name)
        }
    }

    private fun searchRanking() {
        val name = uiState.value.rankingName
        val id = uiState.value.rankingId
        validateRankingName(name = name)
        validateRankingId(id = id)
        if (uiState.value.rankingNameError || uiState.value.rankingIdError) {
            return
        }
        viewModelScope.launch {
            val searchedRankingId = useCases.searchRanking(
                name = name,
                id = id.toLongOrNull() ?: 0L
            )
            _navigationEvent.emit(RankingSearched(id = searchedRankingId))
            resetUiState()
        }
    }

    private fun validateRankingName(name: String) {
        if (name.isEmpty()) {
            showSearchedNameError()
        } else {
            hideSearchedNameError()
        }
    }

    private fun validateRankingId(id: String) {
        if (id.isEmpty()) {
            showSearchedIdError()
        } else {
            hideSearchedIdError()
        }
    }

    private fun updateSearchedName(name: String) {
        _uiState.update { state ->
            state.copy(
                rankingName = name
            )
        }
    }

    private fun updateSearchedId(id: String) {
        _uiState.update { state ->
            state.copy(
                rankingId = id
            )
        }
    }

    private fun showSearchedNameError() {
        _uiState.update { state ->
            state.copy(
                rankingNameError = true
            )
        }
    }

    private fun hideSearchedNameError() {
        _uiState.update { state ->
            state.copy(
                rankingNameError = false
            )
        }
    }

    private fun showSearchedIdError() {
        _uiState.update { state ->
            state.copy(
                rankingIdError = true
            )
        }
    }

    private fun hideSearchedIdError() {
        _uiState.update { state ->
            state.copy(
                rankingIdError = false
            )
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

    private fun resetUiState() {
        _uiState.update { state ->
            state.copy(
                isLoading = false,
                rankingName = "",
                rankingId = ""
            )
        }
    }
}