package com.alpaca.rankify.domain.use_cases.local.rank.delete_ranking

import com.alpaca.rankify.data.repository.Repository

class DeleteRankUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(id: Long): Int = repository.deleteRanking(id = id)
}