package com.alpaca.rankify.data.worker

import android.accounts.NetworkErrorException
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.alpaca.rankify.R
import com.alpaca.rankify.domain.model.CreateRankingDTO
import com.alpaca.rankify.domain.model.mappers.asDto
import com.alpaca.rankify.domain.model.mappers.asExternalModel
import com.alpaca.rankify.domain.use_cases.UseCases
import com.alpaca.rankify.util.Constants.WORK_DATA_LOCAL_RANKING_ID
import com.alpaca.rankify.util.Constants.WORK_DATA_MESSAGE
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

@HiltWorker
class CreateRemoteRankingWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val useCases: UseCases
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val localRankingId = inputData.getLong(WORK_DATA_LOCAL_RANKING_ID, -1)

        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val localRanking = useCases.getRanking(id = localRankingId).firstOrNull()
                    ?: throw IllegalArgumentException(
                        appContext.getString(
                            R.string.oopss_parace_que_o_ranking_nao_existe_mais
                        )
                    )
                requireNotNull(localRanking.adminPassword) {
                    appContext.getString(R.string.ranking_criado_sem_senha_de_administrador)
                }
                val remoteRanking = useCases.createRemoteRanking(
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
                useCases.updateRankingWithPlayers(
                    remoteRanking
                        .copy(isAdmin = true)
                        .asExternalModel(mobileId = localRankingId)
                )
                Result.success(
                    workDataOf(
                        WORK_DATA_MESSAGE to appContext.getString(R.string.ranking_criado_no_servidor_com_sucesso)
                    )
                )
            }.getOrElse { e ->
                when (e) {
                    is SocketTimeoutException -> Result.retry()
                    is NetworkErrorException -> Result.failure(
                        workDataOf(
                            WORK_DATA_MESSAGE to appContext.getString(
                                R.string.network_error,
                                e.localizedMessage
                            )
                        )
                    )

                    else -> Result.failure(
                        workDataOf(
                            WORK_DATA_MESSAGE to appContext.getString(
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