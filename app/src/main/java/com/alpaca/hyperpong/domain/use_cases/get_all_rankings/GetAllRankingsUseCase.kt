package com.alpaca.hyperpong.domain.use_cases.get_all_rankings

import com.alpaca.hyperpong.data.repository.Repository
import com.alpaca.hyperpong.domain.model.Ranking
import kotlinx.coroutines.flow.Flow

class GetAllRankingsUseCase(
    private val repository: Repository
) {
    operator fun invoke(): Flow<List<Ranking>> = repository.getAllRankings()
}