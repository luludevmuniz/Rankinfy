package com.alpaca.hyperpong.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.alpaca.hyperpong.domain.model.mappers.asEntity
import com.alpaca.hyperpong.domain.use_cases.UseCases
import com.alpaca.hyperpong.util.Constants.WORK_DATA_ADMIN_PASSWORD
import com.alpaca.hyperpong.util.Constants.WORK_DATA_LOCAL_RANKING_ID
import com.alpaca.hyperpong.util.Constants.WORK_DATA_RANKING_NAME
import com.alpaca.hyperpong.util.Constants.WORK_DATA_REMOTE_RANKING_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncRankingWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val useCases: UseCases
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val rankingName = inputData.getString(WORK_DATA_RANKING_NAME).orEmpty()
        val remoteId = inputData.getLong(WORK_DATA_REMOTE_RANKING_ID, -1)
        val localId = inputData.getLong(WORK_DATA_LOCAL_RANKING_ID, -1)
        val isAdmin = inputData.getBoolean(WORK_DATA_ADMIN_PASSWORD, false)

        return try {
            val remoteRanking = useCases.getRemoteRanking(
                name = rankingName,
                id = remoteId,
                password = null
            )
            useCases.updateRanking(
                ranking = remoteRanking
                    .asEntity()
                    .copy(
                        localId = localId,
                        isAdmin = isAdmin
                    )
            )
            Result.success(
                workDataOf(
                    "teste" to 0
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}