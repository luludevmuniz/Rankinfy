package com.alpaca.rankify.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alpaca.rankify.domain.model.mappers.asDto
import com.alpaca.rankify.domain.use_cases.UseCases
import com.alpaca.rankify.util.WorkManagerConstants.WorkData.PLAYER_ID
import com.alpaca.rankify.util.WorkManagerConstants.WorkData.REMOTE_RANKING_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class CreateRemotePlayerWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val useCases: UseCases
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val playerId = inputData.getLong(PLAYER_ID, -1)
        val remoteRankingId = inputData.getLong(REMOTE_RANKING_ID, -1)
        return try {
            val player = useCases.getPlayer(id = playerId).firstOrNull() ?: return Result.failure()
            val remotePlayerId = useCases.createRemotePlayer(player = player.asDto(remoteRankingId = remoteRankingId))
            useCases.updatePlayer(player = player.copy(remoteId = remotePlayerId))
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}