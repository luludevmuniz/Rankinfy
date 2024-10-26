package com.alpaca.rankify.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.alpaca.rankify.domain.model.CreateRankingDTO
import com.alpaca.rankify.domain.model.mappers.asDto
import com.alpaca.rankify.domain.model.mappers.asEntity
import com.alpaca.rankify.domain.use_cases.UseCases
import com.alpaca.rankify.util.Constants.WORK_DATA_LOCAL_RANKING_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class CreateRemoteRankingWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val useCases: UseCases
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val localId = inputData.getLong(WORK_DATA_LOCAL_RANKING_ID, -1)

        return try {
            val ranking = useCases.getRank(id = localId).firstOrNull() ?: return Result.failure()
            if (ranking.adminPassword == null) return Result.failure()
            val networkRanking = useCases.createRemoteRank(
                ranking =
                CreateRankingDTO(
                    name = ranking.name,
                    adminPassword = ranking.adminPassword,
                    players = ranking.players.map { player ->
                        player.asDto()
                    }
                )
            )
            useCases.updateRank(networkRanking.copy(isAdmin = true).asEntity(mobileId = localId))
            Result.success(
                workDataOf(
                    "teste" to 0
                )
            )
        } catch (e: Exception) {
            Result.retry()
        }
    }
}