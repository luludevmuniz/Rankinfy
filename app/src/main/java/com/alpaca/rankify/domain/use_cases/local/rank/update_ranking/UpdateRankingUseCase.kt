package com.alpaca.rankify.domain.use_cases.local.rank.update_ranking

import com.alpaca.rankify.data.local.entities.RankingEntity
import com.alpaca.rankify.data.repository.RankingRepository

class UpdateRankingUseCase(
    private val repository: RankingRepository,
) {
    suspend operator fun invoke(ranking: RankingEntity) = repository.updateRanking(ranking = ranking)
}