package com.alpaca.rankify.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.alpaca.rankify.domain.model.CreateRankingDTO
import com.alpaca.rankify.domain.model.mappers.asDto
import com.alpaca.rankify.domain.model.mappers.asExternalModel
import com.alpaca.rankify.domain.use_cases.UseCases
import com.alpaca.rankify.util.Constants.WORK_DATA_LOCAL_RANKING_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

@HiltWorker
class CreateRemoteRankingWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val useCases: UseCases
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val localRankingId = inputData.getLong(WORK_DATA_LOCAL_RANKING_ID, -1)

        return try {
            withContext(Dispatchers.IO) {
                val localRanking = useCases.getRank(id = localRankingId).firstOrNull() ?: return@withContext Result.failure()
                if (localRanking.adminPassword == null) return@withContext Result.failure()
                val remoteRanking = useCases.createRemoteRank(
                    ranking =
                    CreateRankingDTO(
                        name = localRanking.name,
                        adminPassword = localRanking.adminPassword,
                        players = localRanking.players.map { rankingPlayer ->
                            rankingPlayer.asDto()
                        }
                    )
                )
                useCases.deleteAllPlayers(rankingId = localRankingId)
                useCases.updateRankWithPlayers(
                    remoteRanking
                        .copy(isAdmin = true)
                        .asExternalModel(mobileId = localRankingId)
                )
                Result.success(
                    workDataOf(
                        "teste" to 0
                    )
                )
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }
}