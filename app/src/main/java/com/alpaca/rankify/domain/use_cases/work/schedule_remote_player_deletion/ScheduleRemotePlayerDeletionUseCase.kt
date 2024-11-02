package com.alpaca.rankify.domain.use_cases.work.schedule_remote_player_deletion

import androidx.work.WorkInfo
import com.alpaca.rankify.data.repository.Repository
import com.alpaca.rankify.domain.model.Ranking
import kotlinx.coroutines.flow.Flow

class ScheduleRemotePlayerDeletionUseCase(
    private val repository: Repository
) {
    operator fun invoke(
        playerId: Long,
        ranking: Ranking
    ): Flow<WorkInfo?> =
        repository.scheduleRemotePlayerDeletion(
            playerId = playerId,
            ranking = ranking
        )
}