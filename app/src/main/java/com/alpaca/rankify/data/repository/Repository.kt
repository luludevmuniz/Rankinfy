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
import com.alpaca.rankify.data.remote.models.NetworkRanking
import com.alpaca.rankify.data.worker.CreateRemoteRankingWorker
import com.alpaca.rankify.data.worker.SyncRankingWorker
import com.alpaca.rankify.domain.model.CreateRankingDTO
import com.alpaca.rankify.domain.model.Player
import com.alpaca.rankify.domain.model.Ranking
import com.alpaca.rankify.domain.model.mappers.asEntity
import com.alpaca.rankify.domain.model.mappers.asExternalModel
import com.alpaca.rankify.domain.repository.LocalDataSource
import com.alpaca.rankify.domain.repository.RemoteDataSource
import com.alpaca.rankify.util.Constants.UNIQUE_WORK_NAME_CREATE_REMOTE_RANKING
import com.alpaca.rankify.util.Constants.UNIQUE_WORK_NAME_SYNC_REMOTE_RANKING
import com.alpaca.rankify.util.Constants.WORK_DATA_ADMIN_PASSWORD
import com.alpaca.rankify.util.Constants.WORK_DATA_IS_ADMIN
import com.alpaca.rankify.util.Constants.WORK_DATA_LOCAL_RANKING_ID
import com.alpaca.rankify.util.Constants.WORK_DATA_REMOTE_RANKING_ID
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

        fun getRanking(id: Long): Flow<Ranking?> =
            local.getRanking(id = id).map {
                it?.asExternalModel()
            }

        suspend fun createRemoteRanking(ranking: CreateRankingDTO): NetworkRanking = remote.createRanking(ranking = ranking)

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

        suspend fun deleteRanking(id: Long): Int = local.deleteRanking(id = id)

        suspend fun createPlayer(player: Player) {
            local.insertPlayer(player = player.asEntity())
            updatePlayersPositionInRanking(rankingId = player.rankingId)
        }

        suspend fun deletePlayer(player: Player) {
            local.deletePlayer(player = player.asEntity())
            updatePlayersPositionInRanking(rankingId = player.rankingId)
        }

        suspend fun updatePlayer(player: Player) {
            local.updatePlayer(player = player.asEntity())
            updatePlayersPositionInRanking(rankingId = player.rankingId)
        }

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
                "${UNIQUE_WORK_NAME_CREATE_REMOTE_RANKING}_" +
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
                    WORK_DATA_REMOTE_RANKING_ID to ranking.remoteId,
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
                "${UNIQUE_WORK_NAME_SYNC_REMOTE_RANKING}_" +
                    "NAME_${ranking.name}_LOCALID_${ranking.localId}_REMOTEID_${ranking.remoteId}"
            workManager.enqueueUniqueWork(
                workName,
                ExistingWorkPolicy.REPLACE,
                syncRequest,
            )
            return workManager.getWorkInfoByIdFlow(syncRequest.id)
        }

        fun cancelRemoteRankingCreation(
            name: String,
            localId: Long,
        ) {
            val workName =
                "${UNIQUE_WORK_NAME_CREATE_REMOTE_RANKING}_" +
                    "NAME_${name}_LOCALID_$localId"
            workManager.cancelUniqueWork(workName)
        }

        fun cancelSyncRanking(
            name: String,
            localId: Long,
            remoteId: Long,
        ) {
            val workName =
                "${UNIQUE_WORK_NAME_SYNC_REMOTE_RANKING}_" +
                    "NAME_${name}_LOCALID_${localId}_REMOTEID_$remoteId"
            workManager.cancelUniqueWork(workName)
        }
    }
