package com.alpaca.rankify.domain.use_cases.local.rank.get_ranking

import com.alpaca.rankify.data.repository.Repository
import com.alpaca.rankify.domain.model.Ranking
import kotlinx.coroutines.flow.Flow

class GetRankUseCase(
    private val repository: Repository
) {
    operator fun invoke(id: Long): Flow<Ranking?> = repository.getRanking(id = id)
}