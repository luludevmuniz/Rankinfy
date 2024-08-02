package com.alpaca.hyperpong.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.alpaca.hyperpong.domain.model.CreatePlayerDTO
import com.alpaca.hyperpong.domain.model.CreateRankingDTO
import com.alpaca.hyperpong.domain.model.mappers.asEntity
import com.alpaca.hyperpong.domain.use_cases.UseCases
import com.alpaca.hyperpong.util.Constants.WORK_DATA_ADMIN_PASSWORD
import com.alpaca.hyperpong.util.Constants.WORK_DATA_LOCAL_RANKING_ID
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
        val adminPassword = inputData.getString(WORK_DATA_ADMIN_PASSWORD) ?: return Result.failure()

        return try {
            val ranking = useCases.getRanking(id = localId).firstOrNull() ?: return Result.failure()
            val networkRanking = useCases.createRemoteRanking(
                ranking =
                CreateRankingDTO(
                    name = ranking.name,
                    adminPassword = adminPassword,
                    mobileId = localId,
                    players = ranking.players.map { player ->
                        CreatePlayerDTO(
                            name = player.name,
                            score = player.score,
                            rankingId = player.rankingId
                        )
                    }
                )
            )
            useCases.updateRanking(networkRanking.asEntity())
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