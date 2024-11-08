package com.alpaca.rankify.domain.use_cases.work.schedule_remote_player_update

import androidx.work.WorkInfo
import com.alpaca.rankify.data.repository.PlayerRepository
import com.alpaca.rankify.domain.model.Ranking
import com.alpaca.rankify.domain.model.UpdatePlayerDTO
import kotlinx.coroutines.flow.Flow

class ScheduleRemotePlayerUpdateUseCase(
    private val repository: PlayerRepository
) {
    operator fun invoke(
        player: UpdatePlayerDTO,
        ranking: Ranking
    ): Flow<WorkInfo?> =
        repository.scheduleRemotePlayerUpdate(
            player = player,
            ranking = ranking
        )
}