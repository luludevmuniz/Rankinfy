package com.alpaca.hyperpong.presentation.panels.principal.destinations.my_rankings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpaca.hyperpong.domain.model.Ranking
import com.alpaca.hyperpong.domain.use_cases.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyRankingsViewModel @Inject constructor(
    useCases: UseCases
): ViewModel() {
    val savedRankings: StateFlow<List<Ranking>> = useCases.getAllRankings().catch { e ->
        e.printStackTrace()
        //TODO: Logging
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )
}