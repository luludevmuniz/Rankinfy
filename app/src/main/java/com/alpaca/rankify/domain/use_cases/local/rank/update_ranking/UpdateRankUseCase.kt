package com.alpaca.rankify.domain.use_cases.local.rank.update_ranking

import com.alpaca.rankify.data.local.entities.RankingEntity
import com.alpaca.rankify.data.repository.Repository

class UpdateRankUseCase(
    private val repository: Repository,
) {
    suspend operator fun invoke(ranking: RankingEntity) = repository.updateRanking(ranking = ranking)
}