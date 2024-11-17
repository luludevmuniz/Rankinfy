package com.alpaca.rankify.data.worker

import android.accounts.NetworkErrorException
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.alpaca.rankify.R
import com.alpaca.rankify.domain.model.mappers.asExternalModel
import com.alpaca.rankify.domain.use_cases.UseCases
import com.alpaca.rankify.util.constant.WorkManagerConstants.WorkData.IS_ADMIN
import com.alpaca.rankify.util.constant.WorkManagerConstants.WorkData.LOCAL_RANKING_ID
import com.alpaca.rankify.util.constant.WorkManagerConstants.WorkData.MESSAGE
import com.alpaca.rankify.util.constant.WorkManagerConstants.WorkData.REMOTE_RANKING_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

@HiltWorker
class SyncRankingWorker
@AssistedInject
constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val useCases: UseCases,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val remoteId = inputData.getLong(REMOTE_RANKING_ID, -1)
        val localId = inputData.getLong(LOCAL_RANKING_ID, -1)
        val isAdmin = inputData.getBoolean(IS_ADMIN, false)

        return withContext(Dispatchers.IO) {
            runCatching {
                val remoteRanking = useCases.getRemoteRankingUseCase(
                    id = remoteId,
                    password = null,
                )
                useCases.updateRankingWithPlayers(
                    ranking = remoteRanking
                        .asExternalModel(mobileId = localId)
                        .copy(
                            localId = localId,
                            isAdmin = isAdmin,
                        ),
                )
                Result.success(
                    workDataOf(
                        MESSAGE to applicationContext.getString(
                            R.string.ranking_sincronizado_com_sucesso
                        )
                    ),
                )
            }.getOrElse { e ->
                when (e) {
                    is SocketTimeoutException -> Result.retry()

                    is NetworkErrorException -> Result.failure(
                        workDataOf(
                            MESSAGE to applicationContext.getString(
                                R.string.network_error,
                                e.localizedMessage
                            )
                        )
                    )

                    else -> Result.failure(
                        workDataOf(
                            MESSAGE to applicationContext.getString(
                                R.string.um_erro_inesperado_aconteceu,
                                e.localizedMessage
                            )
                        )
                    )
                }
            }
        }
    }
}
