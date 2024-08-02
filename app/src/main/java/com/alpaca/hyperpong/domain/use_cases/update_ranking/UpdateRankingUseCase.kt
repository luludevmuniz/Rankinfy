package com.alpaca.hyperpong.domain.use_cases.update_ranking

import com.alpaca.hyperpong.data.local.entities.RankingEntity
import com.alpaca.hyperpong.data.repository.Repository

class UpdateRankingUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(ranking: RankingEntity) =
        repository.updateRanking(ranking = ranking)
}