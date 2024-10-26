package com.alpaca.rankify.domain.use_cases.work.cancel_sync_rank

import com.alpaca.rankify.data.repository.Repository

class CancelSyncRankUseCase(
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