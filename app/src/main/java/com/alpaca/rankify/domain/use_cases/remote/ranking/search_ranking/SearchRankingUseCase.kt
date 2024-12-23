package com.alpaca.rankify.domain.use_cases.remote.ranking.search_ranking

import com.alpaca.rankify.data.repository.RankingRepository
import com.alpaca.rankify.util.RequestState

class SearchRankingUseCase(
    private val repository: RankingRepository,
) {
    suspend operator fun invoke(
        id: Long,
        adminPassword: String?
    ): RequestState<Long> = repository.searchRanking(
        id = id,
        adminPassword = adminPassword
    )
}