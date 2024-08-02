package com.alpaca.hyperpong.domain.use_cases.get_ranking

import com.alpaca.hyperpong.data.repository.Repository
import com.alpaca.hyperpong.domain.model.Ranking
import kotlinx.coroutines.flow.Flow

class GetRankingUseCase(
    private val repository: Repository
) {
    operator fun invoke(id: Long): Flow<Ranking?> = repository.getRanking(id = id)
}