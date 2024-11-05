package com.alpaca.rankify.domain.use_cases.local.rank.create_ranking

import com.alpaca.rankify.data.repository.Repository

class CreateRankingUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(
        rankingName: String,
        rankingAdminPassword: String
    ): Long =
        repository.createRanking(
            rankingName = rankingName,
            rankingAdminPassword = rankingAdminPassword
        )
}