package com.alpaca.rankify.domain.use_cases.work.schedule_remote_rank_deletion

import androidx.work.WorkInfo
import com.alpaca.rankify.data.repository.Repository
import kotlinx.coroutines.flow.Flow

class ScheduleRemoteRankDeletionUseCase(
    private val repository: Repository
) {
    operator fun invoke(rankId: Long): Flow<WorkInfo?> =
        repository.scheduleRemoteRankDeletion(rankId = rankId)
}