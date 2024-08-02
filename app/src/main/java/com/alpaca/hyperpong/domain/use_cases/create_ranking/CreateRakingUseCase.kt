package com.alpaca.hyperpong.domain.use_cases.create_ranking

import com.alpaca.hyperpong.data.repository.Repository

class CreateRakingUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(rankingName: String): Long =
        repository.createRanking(rankingName = rankingName)
}