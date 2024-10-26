package com.alpaca.rankify.data.repository

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.alpaca.rankify.data.local.entities.PlayerEntity
import com.alpaca.rankify.data.local.entities.RankingEntity
import com.alpaca.rankify.data.local.entities.RankingWithPlayers
import com.alpaca.rankify.data.remote.models.NetworkRanking
import com.alpaca.rankify.data.worker.CreateRemotePlayerWorker
import com.alpaca.rankify.data.worker.CreateRemoteRankingWorker
import com.alpaca.rankify.data.worker.DeleteRemotePlayerWorker
import com.alpaca.rankify.data.worker.DeleteRemoteRankWorker
import com.alpaca.rankify.data.worker.SyncRankingWorker
import com.alpaca.rankify.data.worker.UpdateRemotePlayerWorker
import com.alpaca.rankify.domain.model.CreatePlayerDTO
import com.alpaca.rankify.domain.model.CreateRankingDTO
import com.alpaca.rankify.domain.model.Player
import com.alpaca.rankify.domain.model.Ranking
import com.alpaca.rankify.domain.model.UpdatePlayerDTO
import com.alpaca.rankify.domain.model.mappers.asEntity
import com.alpaca.rankify.domain.model.mappers.asExternalModel
import com.alpaca.rankify.domain.repository.LocalDataSource
import com.alpaca.rankify.domain.repository.RemoteDataSource
import com.alpaca.rankify.util.Constants.UNIQUE_WORK_NAME_CREATE_REMOTE_PLAYER
import com.alpaca.rankify.util.Constants.UNIQUE_WORK_NAME_CREATE_REMOTE_RANK
import com.alpaca.rankify.util.Constants.UNIQUE_WORK_NAME_DELETE_REMOTE_PLAYER
import com.alpaca.rankify.util.Constants.UNIQUE_WORK_NAME_DELETE_REMOTE_RANK
import com.alpaca.rankify.util.Constants.UNIQUE_WORK_NAME_SYNC_REMOTE_RANK
import com.alpaca.rankify.util.Constants.UNIQUE_WORK_NAME_UPDATE_REMOTE_PLAYER
import com.alpaca.rankify.util.Constants.WORK_DATA_ADMIN_PASSWORD
import com.alpaca.rankify.util.Constants.WORK_DATA_IS_ADMIN
import com.alpaca.rankify.util.Constants.WORK_DATA_LOCAL_RANKING_ID
import com.alpaca.rankify.util.Constants.WORK_DATA_PLAYER_ID
import com.alpaca.rankify.util.Constants.WORK_DATA_PLAYER_NAME
import com.alpaca.rankify.util.Constants.WORK_DATA_PLAYER_SCORE
import com.alpaca.rankify.util.Constants.WORK_DATA_REMOTE_RANK_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class Repository
@Inject
constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
    private val workManager: WorkManager,
) {
    suspend fun createRanking(
        rankingName: String,
        rankingAdminPassword: String
    ): Long {
        val rankingEntity =
            RankingEntity(
                name = rankingName,
                isAdmin = true,
                adminPassword = rankingAdminPassword
            )
        return local.saveRanking(ranking = rankingEntity)
    }

    suspend fun updateRanking(ranking: RankingEntity) = local.updateRanking(ranking = ranking)

    suspend fun updateRankingWithPlayers(ranking: Ranking) {
        val rankingWithPlayers = RankingWithPlayers(
            ranking = ranking.asEntity(),
            players = ranking.players.map { it.asEntity() }
        )
        local.updateRankingWithPlayers(rankingWithPlayers = rankingWithPlayers)
    }

    fun getRanking(id: Long): Flow<Ranking?> =
        local.getRanking(id = id).map {
            it?.asExternalModel()
        }

    suspend fun createRemoteRanking(ranking: CreateRankingDTO): NetworkRanking =
        remote.createRanking(ranking = ranking)

    fun getAllRankings(): Flow<List<Ranking>> =
        local.getAllRankings().map {
            it.map { entity ->
                entity.asExternalModel()
            }
        }

    suspend fun getRemoteRanking(
        id: Long,
        password: String? = null,
    ): NetworkRanking {
        val ranking =
            remote.getRanking(
                id = id,
                password = password,
            )
        return ranking
    }

    suspend fun createRemotePlayer(player: CreatePlayerDTO) =
        remote.createPlayer(player = player)

    suspend fun deleteRanking(id: Long): Int = local.deleteRanking(id = id)

    suspend fun deleteRemoteRank(id: Long) = remote.deleteRanking(id = id)

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

    suspend fun deleteRemotePlayer(id: Long) = remote.deletePlayer(id = id)

    suspend fun updatePlayer(player: Player) {
        local.updatePlayer(player = player.asEntity())
        updatePlayersPositionInRanking(rankingId = player.rankingId)
    }

    suspend fun updateRemotePlayer(player: UpdatePlayerDTO) = remote.updatePlayer(player = player)

    suspend fun searchRanking(id: Long): Long {
        val ranking = remote.getRanking(id = id)
        return local.saveRanking(ranking = ranking.asEntity())
    }

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

    fun scheduleRemoteRankingCreation(ranking: CreateRankingDTO): Flow<WorkInfo> {
        val data =
            workDataOf(
                WORK_DATA_LOCAL_RANKING_ID to ranking.mobileId,
                WORK_DATA_ADMIN_PASSWORD to ranking.adminPassword,
            )

        val constraints =
            Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val createRemoteRankingRequest =
            OneTimeWorkRequestBuilder<CreateRemoteRankingWorker>()
                .setInputData(data)
                .setConstraints(constraints)
                .build()
        val workName =
            "${UNIQUE_WORK_NAME_CREATE_REMOTE_RANK}_" +
                    "NAME_${ranking.name}_LOCALID_${ranking.mobileId}"
        workManager.enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            createRemoteRankingRequest,
        )
        return workManager.getWorkInfoByIdFlow(createRemoteRankingRequest.id)
    }

    fun scheduleSyncRanking(ranking: Ranking): Flow<WorkInfo> {
        val data =
            workDataOf(
                WORK_DATA_LOCAL_RANKING_ID to ranking.localId,
                WORK_DATA_REMOTE_RANK_ID to ranking.remoteId,
                WORK_DATA_IS_ADMIN to ranking.isAdmin,
            )

        val constraints =
            Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val syncRequest =
            OneTimeWorkRequestBuilder<SyncRankingWorker>()
                .setInputData(data)
                .setConstraints(constraints)
                .build()
        val workName =
            "${UNIQUE_WORK_NAME_SYNC_REMOTE_RANK}_" +
                    "NAME_${ranking.name}_LOCALID_${ranking.localId}_REMOTEID_${ranking.remoteId}"
        workManager.enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            syncRequest,
        )
        return workManager.getWorkInfoByIdFlow(syncRequest.id)
    }

    fun scheduleRemotePlayerCreation(
        playerId: Long,
        remoteRankingId: Long
    ): Flow<WorkInfo> {
        val data =
            workDataOf(
                WORK_DATA_PLAYER_ID to playerId,
                WORK_DATA_REMOTE_RANK_ID to remoteRankingId
            )
        val constraints =
            Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val workName = "${UNIQUE_WORK_NAME_CREATE_REMOTE_PLAYER}_" + "PLAYER_ID_$playerId"
        val createRemotePlayerRequest =
            OneTimeWorkRequestBuilder<CreateRemotePlayerWorker>()
                .setInputData(data)
                .setConstraints(constraints)
                .build()
        workManager.enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            createRemotePlayerRequest,
        )
        return workManager.getWorkInfoByIdFlow(createRemotePlayerRequest.id)
    }

    fun scheduleRemotePlayerDeletion(playerId: Long): Flow<WorkInfo> {
        val data = workDataOf(WORK_DATA_PLAYER_ID to playerId)
        val constraints =
            Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val workName = "${UNIQUE_WORK_NAME_DELETE_REMOTE_PLAYER}_" + "PLAYER_ID_$playerId"
        val deleteRemotePlayerRequest =
            OneTimeWorkRequestBuilder<DeleteRemotePlayerWorker>()
                .setInputData(data)
                .setConstraints(constraints)
                .build()
        workManager.enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            deleteRemotePlayerRequest,
        )
        return workManager.getWorkInfoByIdFlow(deleteRemotePlayerRequest.id)
    }

    fun scheduleRemoteRankDeletion(rankId: Long): Flow<WorkInfo> {
        val data = workDataOf(WORK_DATA_REMOTE_RANK_ID to rankId)
        val constraints =
            Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val workName = "${UNIQUE_WORK_NAME_DELETE_REMOTE_RANK}_" + "RANK_ID_$rankId"
        val deleteRemoteRankRequest =
            OneTimeWorkRequestBuilder<DeleteRemoteRankWorker>()
                .setInputData(data)
                .setConstraints(constraints)
                .build()
        workManager.enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            deleteRemoteRankRequest,
        )
        return workManager.getWorkInfoByIdFlow(deleteRemoteRankRequest.id)
    }

    fun scheduleRemotePlayerUpdate(player: UpdatePlayerDTO): Flow<WorkInfo> {
        val data = workDataOf(
            WORK_DATA_PLAYER_NAME to player.name,
            WORK_DATA_PLAYER_SCORE to player.score,
            WORK_DATA_PLAYER_ID to player.id
        )
        val constraints =
            Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val workName =
            "${UNIQUE_WORK_NAME_UPDATE_REMOTE_PLAYER}_" +
                    "PLAYER_ID:${player.id}_" +
                    "PLAYER_NAME:${player.name}_" +
                    "PLAYER_SCORE:${player.score}"
        val updateRemotePlayerRequest =
            OneTimeWorkRequestBuilder<UpdateRemotePlayerWorker>()
                .setInputData(data)
                .setConstraints(constraints)
                .build()
        workManager.enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            updateRemotePlayerRequest,
        )
        return workManager.getWorkInfoByIdFlow(updateRemotePlayerRequest.id)
    }

    fun cancelRemoteRankingCreation(
        name: String,
        localId: Long,
    ) {
        val workName =
            "${UNIQUE_WORK_NAME_CREATE_REMOTE_RANK}_" +
                    "NAME_${name}_LOCALID_$localId"
        workManager.cancelUniqueWork(workName)
    }

    fun cancelSyncRanking(
        name: String,
        localId: Long,
        remoteId: Long,
    ) {
        val workName =
            "${UNIQUE_WORK_NAME_SYNC_REMOTE_RANK}_" +
                    "NAME_${name}_LOCALID_${localId}_REMOTEID_$remoteId"
        workManager.cancelUniqueWork(workName)
    }
}
