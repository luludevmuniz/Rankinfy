package com.alpaca.rankify.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.alpaca.rankify.domain.model.mappers.asExternalModel
import com.alpaca.rankify.domain.use_cases.UseCases
import com.alpaca.rankify.util.Constants.WORK_DATA_IS_ADMIN
import com.alpaca.rankify.util.Constants.WORK_DATA_LOCAL_RANKING_ID
import com.alpaca.rankify.util.Constants.WORK_DATA_REMOTE_RANK_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncRankingWorker
    @AssistedInject
    constructor(
        @Assisted appContext: Context,
        @Assisted workerParams: WorkerParameters,
        private val useCases: UseCases,
    ) : CoroutineWorker(appContext, workerParams) {
        override suspend fun doWork(): Result {
            val remoteId = inputData.getLong(WORK_DATA_REMOTE_RANK_ID, -1)
            val localId = inputData.getLong(WORK_DATA_LOCAL_RANKING_ID, -1)
            val isAdmin = inputData.getBoolean(WORK_DATA_IS_ADMIN, false)

            return try {
                val remoteRanking =
                    useCases.getRemoteRankingUseCase(
                        id = remoteId,
                        password = null,
                    )
                useCases.updateRankWithPlayers(
                    ranking =
                        remoteRanking
                            .asExternalModel(mobileId = localId)
                            .copy(
                                localId = localId,
                                isAdmin = isAdmin,
                            ),
                )
                Log.d("SYNC WORK", "SUCCESS")
                Result.success(
                    workDataOf(
                        "teste" to 0,
                    ),
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Result.retry()
            }
        }
    }
