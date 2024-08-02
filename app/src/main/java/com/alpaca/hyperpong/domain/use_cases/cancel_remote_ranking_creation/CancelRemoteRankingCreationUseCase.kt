package com.alpaca.hyperpong.domain.use_cases.cancel_remote_ranking_creation

import com.alpaca.hyperpong.data.repository.Repository

class CancelRemoteRankingCreationUseCase(
    private val repository: Repository
) {
    operator fun invoke(
        name: String,
        localId: Long
    ) =
        repository.cancelRemoteRankingCreation(
            name = name,
            localId = localId
        )
}