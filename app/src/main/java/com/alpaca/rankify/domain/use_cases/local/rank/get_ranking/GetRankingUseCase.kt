package com.alpaca.rankify.domain.use_cases.local.rank.get_ranking

import com.alpaca.rankify.data.repository.RankingRepository
import com.alpaca.rankify.domain.model.Ranking
import kotlinx.coroutines.flow.Flow

class GetRankingUseCase(
    private val repository: RankingRepository
) {
    operator fun invoke(id: Long): Flow<Ranking?> = repository.getRanking(id = id)
}