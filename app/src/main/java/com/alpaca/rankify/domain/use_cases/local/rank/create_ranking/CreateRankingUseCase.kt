package com.alpaca.rankify.domain.use_cases.local.rank.create_ranking

import com.alpaca.rankify.data.repository.RankingRepository
import com.alpaca.rankify.util.RequestState

class CreateRankingUseCase(
    private val repository: RankingRepository
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