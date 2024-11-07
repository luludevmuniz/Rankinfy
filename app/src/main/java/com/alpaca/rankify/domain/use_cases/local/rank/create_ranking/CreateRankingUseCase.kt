package com.alpaca.rankify.domain.use_cases.local.rank.create_ranking

import com.alpaca.rankify.data.repository.Repository
import com.alpaca.rankify.util.RequestState

class CreateRankingUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(
        rankingName: String,
        rankingAdminPassword: String
    ): RequestState<Long> =
        repository.createRanking(
            rankingName = rankingName,
            rankingAdminPassword = rankingAdminPassword
        )
}