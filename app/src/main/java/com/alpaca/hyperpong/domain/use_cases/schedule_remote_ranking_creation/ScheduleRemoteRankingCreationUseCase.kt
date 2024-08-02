package com.alpaca.hyperpong.domain.use_cases.schedule_remote_ranking_creation

import androidx.work.WorkInfo
import com.alpaca.hyperpong.data.repository.Repository
import com.alpaca.hyperpong.domain.model.CreateRankingDTO
import kotlinx.coroutines.flow.Flow

class ScheduleRemoteRankingCreationUseCase(
    private val repository: Repository
) {
    operator fun invoke(ranking: CreateRankingDTO): Flow<WorkInfo> =
        repository.scheduleRemoteRankingCreation(ranking = ranking)
}