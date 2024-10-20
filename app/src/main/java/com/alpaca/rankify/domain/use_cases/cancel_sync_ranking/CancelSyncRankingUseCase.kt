package com.alpaca.rankify.domain.use_cases.cancel_sync_ranking

import com.alpaca.rankify.data.repository.Repository

class CancelSyncRankingUseCase(
    private val repository: Repository
) {
    operator fun invoke(
        name: String,
        localId: Long,
        remoteId: Long
    ) =
        repository.cancelSyncRanking(
            name = name,
            localId = localId,
            remoteId = remoteId
        )
}