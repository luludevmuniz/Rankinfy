package com.alpaca.rankify.domain.use_cases.work.schedule_remote_rank_creation

import androidx.work.WorkInfo
import com.alpaca.rankify.data.repository.RankingRepository
import com.alpaca.rankify.domain.model.CreateRankingDTO
import kotlinx.coroutines.flow.Flow

class ScheduleRemoteRankingCreationUseCase(
    private val repository: RankingRepository
) {
    operator fun invoke(ranking: CreateRankingDTO): Flow<WorkInfo?> =
        repository.scheduleRemoteRankingCreation(ranking = ranking)
}