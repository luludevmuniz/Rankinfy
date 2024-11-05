package com.alpaca.rankify.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alpaca.rankify.domain.use_cases.UseCases
import com.alpaca.rankify.util.Constants.WORK_DATA_REMOTE_RANK_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DeleteRemoteRankingWorker @AssistedInject
constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val useCases: UseCases,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val rankId = inputData.getLong(WORK_DATA_REMOTE_RANK_ID, -1)
        return try {
            useCases.deleteRemoteRanking(id = rankId)
            Result.success()
        } catch (e: Exception) {
            println(e)
            Result.retry()
        }
    }
}