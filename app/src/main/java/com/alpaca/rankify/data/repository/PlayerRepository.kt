package com.alpaca.rankify.data.repository

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.alpaca.rankify.data.local.entities.PlayerEntity
import com.alpaca.rankify.data.worker.CreateRemotePlayerWorker
import com.alpaca.rankify.data.worker.DeleteRemotePlayerWorker
import com.alpaca.rankify.data.worker.SyncRankingWorker
import com.alpaca.rankify.data.worker.UpdateRemotePlayerWorker
import com.alpaca.rankify.domain.model.CreatePlayerDTO
import com.alpaca.rankify.domain.model.Player
import com.alpaca.rankify.domain.model.Ranking
import com.alpaca.rankify.domain.model.UpdatePlayerDTO
import com.alpaca.rankify.domain.model.mappers.asEntity
import com.alpaca.rankify.domain.model.mappers.asExternalModel
import com.alpaca.rankify.domain.repository.LocalDataSource
import com.alpaca.rankify.domain.repository.RemoteDataSource
import com.alpaca.rankify.util.WorkManagerConstants.UniqueWorkName.CREATE_REMOTE_PLAYER
import com.alpaca.rankify.util.WorkManagerConstants.UniqueWorkName.DELETE_REMOTE_PLAYER
import com.alpaca.rankify.util.WorkManagerConstants.UniqueWorkName.SYNC_REMOTE_RANKING
import com.alpaca.rankify.util.WorkManagerConstants.UniqueWorkName.UPDATE_REMOTE_PLAYER
import com.alpaca.rankify.util.WorkManagerConstants.WORK_MANAGER_DEFAULT_CONSTRAINTS
import com.alpaca.rankify.util.WorkManagerConstants.WorkData.IS_ADMIN
import com.alpaca.rankify.util.WorkManagerConstants.WorkData.LOCAL_RANKING_ID
import com.alpaca.rankify.util.WorkManagerConstants.WorkData.PLAYER_ID
import com.alpaca.rankify.util.WorkManagerConstants.WorkData.PLAYER_NAME
import com.alpaca.rankify.util.WorkManagerConstants.WorkData.PLAYER_SCORE
import com.alpaca.rankify.util.WorkManagerConstants.WorkData.REMOTE_RANKING_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerRepository
@Inject
constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
    private val workManager: WorkManager,
) {
    fun getPlayer(id: Long): Flow<Player?> = local.getPlayer(id = id).map {
        it?.asExternalModel()
    }

    suspend fun createPlayer(player: Player): Long {
        val id = local.insertPlayer(player = player.asEntity())
        updatePlayersPositionInRanking(rankingId = player.rankingId)
        return id
    }

    suspend fun deletePlayer(player: Player) {
        local.deletePlayer(player = player.asEntity())
        updatePlayersPositionInRanking(rankingId = player.rankingId)
    }

    suspend fun deleteAllPlayers(rankingId: Long) {
        local.deleteAllPlayers(rankingId = rankingId)
    }

    suspend fun deleteRemotePlayer(id: Long) = remote.deletePlayer(id = id)

    suspend fun updatePlayer(player: Player) {
        local.updatePlayer(player = player.asEntity())
        updatePlayersPositionInRanking(rankingId = player.rankingId)
    }

    suspend fun updateRemotePlayer(player: UpdatePlayerDTO) = remote.updatePlayer(player = player)

    private suspend fun updatePlayersPositionInRanking(rankingId: Long) {
        val players = local.getPlayersByRanking(rankingId = rankingId)
        players
            .sortedByDescending {
                it.score.toLong()
            }.forEachIndexed { index, player ->
                local.updatePlayer(
                    player =
                    PlayerEntity(
                        localId = player.localId,
                        name = player.name,
                        score = player.score,
                        rankingId = player.rankingId,
                        remoteId = player.remoteId,
                        currentRankingPosition = index.plus(1),
                        previousRankingPosition = player.currentRankingPosition,
                    ),
                )
            }
    }

    suspend fun createRemotePlayer(player: CreatePlayerDTO) =
        remote.createPlayer(player = player)

    /**
     * Cria um Worker para criar um novo jogador na API.
     * O Worker aguarda até que haja conexão com à internet para executar.
     * Esta função somente é chamado quando o ranking já foi criado remotamente, ou seja, quando o
     * remoteId do ranking existe e é diferente de 0, pois do contrário o jogador já será criado
     * quando o CreateRemoteRankingWorker executar, visto que ele busca no Room o ranking a ser
     * criado, junto com o jogador que seria criado aqui pois o jogador já foi criado localmente.
     * Caso exista uma solicitação de sincronização pendente (o usuário pode estar sem conexão à
     * internet, por exemplo), esta função cancela tal solicitação e cria uma nova para ser executada
     * após a criação do jogador remotamente.
     * @param playerId ID do jogador a ser criado.
     * @param ranking Ranking em que o jogador será criado, para criar uma nova
     * solicitação de sincronização, caso necessário.
     * */
    fun scheduleRemotePlayerCreation(
        playerId: Long,
        ranking: Ranking
    ): Flow<WorkInfo?> {
        val data =
            workDataOf(
                PLAYER_ID to playerId,
                REMOTE_RANKING_ID to ranking.remoteId
            )

        val createPlayerWorkerName =
            "${CREATE_REMOTE_PLAYER}_" + "PLAYER_ID_$playerId"

        val createRemotePlayerRequest =
            OneTimeWorkRequestBuilder<CreateRemotePlayerWorker>()
                .setInputData(data)
                .setConstraints(WORK_MANAGER_DEFAULT_CONSTRAINTS)
                .build()

        val syncWorkerTag = "$SYNC_REMOTE_RANKING${ranking.remoteId}"

        if (workManager.getWorkInfosByTag(syncWorkerTag).get().isNotEmpty()) {
            workManager.cancelAllWorkByTag(syncWorkerTag)
            val syncWorkerData = workDataOf(
                LOCAL_RANKING_ID to ranking.localId,
                REMOTE_RANKING_ID to ranking.remoteId,
                IS_ADMIN to ranking.isAdmin,
            )
            val syncRequest =
                OneTimeWorkRequestBuilder<SyncRankingWorker>()
                    .setInputData(syncWorkerData)
                    .setConstraints(WORK_MANAGER_DEFAULT_CONSTRAINTS)
                    .addTag(syncWorkerTag)
                    .build()
            workManager
                .beginUniqueWork(
                    createPlayerWorkerName,
                    ExistingWorkPolicy.REPLACE,
                    createRemotePlayerRequest,
                )
                .then(syncRequest)
                .enqueue()
        } else {
            workManager.enqueueUniqueWork(
                createPlayerWorkerName,
                ExistingWorkPolicy.REPLACE,
                createRemotePlayerRequest,
            )
        }

        return workManager.getWorkInfoByIdFlow(createRemotePlayerRequest.id)
    }
    fun scheduleRemotePlayerDeletion(
        playerId: Long,
        ranking: Ranking
    ): Flow<WorkInfo?> {
        val data = workDataOf(PLAYER_ID to playerId)

        val workName = "${DELETE_REMOTE_PLAYER}_" + "PLAYER_ID_$playerId"

        val deleteRemotePlayerRequest =
            OneTimeWorkRequestBuilder<DeleteRemotePlayerWorker>()
                .setInputData(data)
                .setConstraints(WORK_MANAGER_DEFAULT_CONSTRAINTS)
                .build()

        val syncWorkerTag = "$SYNC_REMOTE_RANKING${ranking.remoteId}"

        if (workManager.getWorkInfosByTag(syncWorkerTag).get().isNotEmpty()) {

            workManager.cancelAllWorkByTag(syncWorkerTag)

            val syncWorkerData = workDataOf(
                LOCAL_RANKING_ID to ranking.localId,
                REMOTE_RANKING_ID to ranking.remoteId,
                IS_ADMIN to ranking.isAdmin,
            )
            val syncRequest =
                OneTimeWorkRequestBuilder<SyncRankingWorker>()
                    .setInputData(syncWorkerData)
                    .setConstraints(WORK_MANAGER_DEFAULT_CONSTRAINTS)
                    .addTag(syncWorkerTag)
                    .build()
            workManager
                .beginUniqueWork(
                    workName,
                    ExistingWorkPolicy.REPLACE,
                    deleteRemotePlayerRequest,
                )
                .then(syncRequest)
                .enqueue()
        } else {
            workManager.enqueueUniqueWork(
                workName,
                ExistingWorkPolicy.REPLACE,
                deleteRemotePlayerRequest,
            )
        }
        return workManager.getWorkInfoByIdFlow(deleteRemotePlayerRequest.id)
    }

    fun scheduleRemotePlayerUpdate(
        player: UpdatePlayerDTO,
        ranking: Ranking
    ): Flow<WorkInfo?> {
        val data = workDataOf(
            PLAYER_NAME to player.name,
            PLAYER_SCORE to player.score,
            PLAYER_ID to player.id
        )

        val workName =
            "${UPDATE_REMOTE_PLAYER}_" +
                    "PLAYER_ID:${player.id}_" +
                    "PLAYER_NAME:${player.name}_" +
                    "PLAYER_SCORE:${player.score}"

        val updateRemotePlayerRequest =
            OneTimeWorkRequestBuilder<UpdateRemotePlayerWorker>()
                .setInputData(data)
                .setConstraints(WORK_MANAGER_DEFAULT_CONSTRAINTS)
                .build()

        val syncWorkerTag = "$SYNC_REMOTE_RANKING${ranking.remoteId}"

        if (workManager.getWorkInfosByTag(syncWorkerTag).get().isNotEmpty()) {
            workManager.cancelAllWorkByTag(syncWorkerTag)

            val syncWorkerData = workDataOf(
                LOCAL_RANKING_ID to ranking.localId,
                REMOTE_RANKING_ID to ranking.remoteId,
                IS_ADMIN to ranking.isAdmin,
            )
            val syncRequest =
                OneTimeWorkRequestBuilder<SyncRankingWorker>()
                    .setInputData(syncWorkerData)
                    .setConstraints(WORK_MANAGER_DEFAULT_CONSTRAINTS)
                    .addTag(syncWorkerTag)
                    .build()
            workManager
                .beginUniqueWork(
                    workName,
                    ExistingWorkPolicy.REPLACE,
                    updateRemotePlayerRequest,
                )
                .then(syncRequest)
                .enqueue()
        } else {
            workManager.enqueueUniqueWork(
                workName,
                ExistingWorkPolicy.REPLACE,
                updateRemotePlayerRequest,
            )
        }
        return workManager.getWorkInfoByIdFlow(updateRemotePlayerRequest.id)
    }
}