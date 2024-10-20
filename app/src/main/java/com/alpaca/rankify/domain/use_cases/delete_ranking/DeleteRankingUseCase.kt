package com.alpaca.rankify.domain.use_cases.delete_ranking

import com.alpaca.rankify.data.repository.Repository

class DeleteRankingUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(id: Long): Int = repository.deleteRanking(id = id)
}