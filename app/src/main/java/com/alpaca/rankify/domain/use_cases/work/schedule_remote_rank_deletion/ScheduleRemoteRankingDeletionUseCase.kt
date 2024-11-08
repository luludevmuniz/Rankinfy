package com.alpaca.rankify.domain.use_cases.work.schedule_remote_rank_deletion

import androidx.work.WorkInfo
import com.alpaca.rankify.data.repository.RankingRepository
import kotlinx.coroutines.flow.Flow

class ScheduleRemoteRankingDeletionUseCase(
    private val repository: RankingRepository
) {
    operator fun invoke(rankId: Long): Flow<WorkInfo?> =
        repository.scheduleRemoteRankDeletion(rankId = rankId)
}