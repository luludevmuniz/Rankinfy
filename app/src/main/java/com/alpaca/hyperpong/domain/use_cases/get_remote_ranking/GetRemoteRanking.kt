package com.alpaca.hyperpong.domain.use_cases.get_remote_ranking

import com.alpaca.hyperpong.data.remote.models.NetworkRanking
import com.alpaca.hyperpong.data.repository.Repository

class GetRemoteRanking(
    private val repository: Repository
) {
    suspend operator fun invoke(
        name: String,
        id: Long,
        password: String? = null
    ): NetworkRanking =
        repository.getRemoteRanking(
            name,
            id,
            password
        )
}