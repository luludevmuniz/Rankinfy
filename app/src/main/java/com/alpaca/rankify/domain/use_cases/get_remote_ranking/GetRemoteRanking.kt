package com.alpaca.rankify.domain.use_cases.get_remote_ranking

import com.alpaca.rankify.data.remote.models.NetworkRanking
import com.alpaca.rankify.data.repository.Repository

class GetRemoteRanking(
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