package com.alpaca.rankify.domain.use_cases.work.cancel_remote_rank_creation

import com.alpaca.rankify.data.repository.Repository

class CancelRemoteRankCreationUseCase(
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