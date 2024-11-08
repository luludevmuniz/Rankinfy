package com.alpaca.rankify.domain.use_cases.local.rank.delete_ranking

import com.alpaca.rankify.data.repository.RankingRepository

class DeleteRankingUseCase(
    private val repository: RankingRepository
) {
    suspend operator fun invoke(id: Long): Int = repository.deleteRanking(id = id)
}