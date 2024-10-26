package com.alpaca.rankify.presentation.panels.principal.destinations.my_rankings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpaca.rankify.domain.model.Ranking
import com.alpaca.rankify.domain.use_cases.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRankingsViewModel
@Inject
constructor(
    private val useCases: UseCases,
) : ViewModel() {
    val savedRankings: StateFlow<List<Ranking>> =
        useCases
            .getAllRanks()
            .catch { e ->
                e.printStackTrace()
                // TODO: Logging
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList(),
            )

    fun deleteRanking(id: Long) {
        viewModelScope.launch {
            useCases.deleteRank(id)
        }
    }
}
