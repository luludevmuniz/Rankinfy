package com.alpaca.rankify.domain.use_cases.remote.rank.get_remote_rank

import com.alpaca.rankify.data.remote.models.NetworkRanking
import com.alpaca.rankify.data.repository.Repository

class GetRemoteRank(
    private val repository: Repository
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