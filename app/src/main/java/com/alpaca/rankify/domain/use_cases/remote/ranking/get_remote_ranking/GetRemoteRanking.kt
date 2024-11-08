package com.alpaca.rankify.domain.use_cases.remote.ranking.get_remote_ranking

import com.alpaca.rankify.data.remote.models.NetworkRanking
import com.alpaca.rankify.data.repository.RankingRepository

class GetRemoteRanking(
    private val repository: RankingRepository
) {
    suspend operator fun invoke(
        id: Long,
        password: String? = null
    ): NetworkRanking =
        repository.getRemoteRanking(
            id,
            password
        )
}