package com.alpaca.rankify.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alpaca.rankify.domain.model.UpdatePlayerDTO
import com.alpaca.rankify.domain.use_cases.UseCases
import com.alpaca.rankify.util.Constants.WORK_DATA_PLAYER_ID
import com.alpaca.rankify.util.Constants.WORK_DATA_PLAYER_NAME
import com.alpaca.rankify.util.Constants.WORK_DATA_PLAYER_SCORE
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateRemotePlayerWorker @AssistedInject
constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val useCases: UseCases,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val id = inputData.getLong(WORK_DATA_PLAYER_ID, -1)
        val name = inputData.getString(WORK_DATA_PLAYER_NAME).orEmpty()
        val score = inputData.getString(WORK_DATA_PLAYER_SCORE).orEmpty()
        return try {
            useCases.updateRemotePlayer(
                player = UpdatePlayerDTO(
                    id = id,
                    name = name,
                    score = score,
                )
            )
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}