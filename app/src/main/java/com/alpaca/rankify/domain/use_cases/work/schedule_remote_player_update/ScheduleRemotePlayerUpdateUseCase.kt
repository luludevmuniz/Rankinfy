package com.alpaca.rankify.domain.use_cases.work.schedule_remote_player_update

import androidx.work.WorkInfo
import com.alpaca.rankify.data.repository.Repository
import com.alpaca.rankify.domain.model.UpdatePlayerDTO
import kotlinx.coroutines.flow.Flow

class ScheduleRemotePlayerUpdateUseCase(
    private val repository: Repository
) {
    operator fun invoke(player: UpdatePlayerDTO): Flow<WorkInfo> =
        repository.scheduleRemotePlayerUpdate(player = player)
}