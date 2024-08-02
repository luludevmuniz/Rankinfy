package com.alpaca.hyperpong.domain.use_cases.delete_ranking

import com.alpaca.hyperpong.data.repository.Repository

class DeleteRankingUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(id: Long): Int = repository.deleteRanking(id = id)
}