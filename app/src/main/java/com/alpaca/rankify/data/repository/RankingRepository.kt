package com.alpaca.rankify.data.repository

import android.accounts.NetworkErrorException
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.alpaca.rankify.data.local.entities.RankingEntity
import com.alpaca.rankify.data.local.entities.RankingWithPlayers
import com.alpaca.rankify.data.remote.models.NetworkRanking
import com.alpaca.rankify.data.worker.CreateRemoteRankingWorker
import com.alpaca.rankify.data.worker.DeleteRemoteRankingWorker
import com.alpaca.rankify.data.worker.SyncRankingWorker
import com.alpaca.rankify.domain.model.CreateRankingDTO
import com.alpaca.rankify.domain.model.Ranking
import com.alpaca.rankify.domain.model.mappers.asEntity
import com.alpaca.rankify.domain.model.mappers.asExternalModel
import com.alpaca.rankify.domain.repository.LocalDataSource
import com.alpaca.rankify.domain.repository.RemoteDataSource
import com.alpaca.rankify.util.RequestState
import com.alpaca.rankify.util.constant.WorkManagerConstants.UniqueWorkName.CREATE_REMOTE_RANKING
import com.alpaca.rankify.util.constant.WorkManagerConstants.UniqueWorkName.DELETE_REMOTE_RANKING
import com.alpaca.rankify.util.constant.WorkManagerConstants.UniqueWorkName.SYNC_REMOTE_RANKING
import com.alpaca.rankify.util.constant.WorkManagerConstants.WORK_MANAGER_DEFAULT_CONSTRAINTS
import com.alpaca.rankify.util.constant.WorkManagerConstants.WorkData.ADMIN_PASSWORD
import com.alpaca.rankify.util.constant.WorkManagerConstants.WorkData.IS_ADMIN
import com.alpaca.rankify.util.constant.WorkManagerConstants.WorkData.LOCAL_RANKING_ID
import com.alpaca.rankify.util.constant.WorkManagerConstants.WorkData.REMOTE_RANKING_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RankingRepository
@Inject constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
    private val workManager: WorkManager,
) {
    suspend fun createRanking(
        rankingName: String,
        rankingAdminPassword: String
    ): RequestState<Long> {
        return try {
            val rankingEntity =
                RankingEntity(
                    name = rankingName,
                    isAdmin = true,
                    adminPassword = rankingAdminPassword
                )
            RequestState.Success(local.saveRanking(ranking = rankingEntity))
        } catch (e: Exception) {
            RequestState.Error("An error occurred: ${e.message}")
        }
    }

    suspend fun updateRanking(ranking: RankingEntity) = local.updateRanking(ranking = ranking)

    suspend fun updateRankingWithPlayers(ranking: Ranking) {
        val rankingWithPlayers = RankingWithPlayers(
            ranking = ranking.asEntity(),
            players = ranking.players.map {
                it.asEntity()
            }
        )
        local.updateRankingWithPlayers(rankingWithPlayers = rankingWithPlayers)
    }

    suspend fun saveRankingWithPlayers(ranking: Ranking): Long {
        val rankingWithPlayers = RankingWithPlayers(
            ranking = ranking.asEntity(),
            players = ranking.players.map { it.asEntity() }
        )
        return local.saveRankingWithPlayers(rankingWithPlayers = rankingWithPlayers)
    }

    fun getRanking(id: Long): Flow<Ranking?> =
        local.getRanking(id = id).map {
            it?.asExternalModel()
        }

    fun getRankingWithRemoteId(id: Long): Flow<Ranking?> =
        local.getRankingWithRemoteId(id = id).map {
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

    suspend fun deleteRanking(id: Long): Int = local.deleteRanking(id = id)

    suspend fun deleteRemoteRanking(id: Long) = remote.deleteRanking(id = id)

    suspend fun searchRanking(
        id: Long,
        adminPassword: String?
    ): RequestState<Long> = try {
        RequestState.Success(
            fetchAndSaveRanking(
                id = id,
                adminPassword = adminPassword
            )
        )
    } catch (e: NetworkErrorException) {
        RequestState.Error("Network error: ${e.localizedMessage}")
    } catch (e: Exception) {
        RequestState.Error("An error occurred: ${e.localizedMessage}")
    }

    private suspend fun fetchAndSaveRanking(
        id: Long,
        adminPassword: String?
    ): Long {
        val ranking = remote.getRanking(
            id = id,
            password = adminPassword
        )
        val existingRanking = getRankingWithRemoteId(ranking.apiId).firstOrNull()
        return existingRanking?.localId
            ?: saveRankingWithPlayers(ranking = ranking.asExternalModel(mobileId = 0))
    }

    fun scheduleRemoteRankingCreation(ranking: CreateRankingDTO): Flow<WorkInfo?> {
        val data =
            workDataOf(
                LOCAL_RANKING_ID to ranking.mobileId,
                ADMIN_PASSWORD to ranking.adminPassword,
            )

        val createRemoteRankingRequest =
            OneTimeWorkRequestBuilder<CreateRemoteRankingWorker>()
                .setInputData(data)
                .setConstraints(WORK_MANAGER_DEFAULT_CONSTRAINTS)
                .addTag("$CREATE_REMOTE_RANKING${ranking.mobileId}")
                .build()

        val workName =
            "${CREATE_REMOTE_RANKING}_" +
                    "NAME:${ranking.name}_LOCALID:${ranking.mobileId}"

        workManager.enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            createRemoteRankingRequest,
        )

        return workManager.getWorkInfoByIdFlow(createRemoteRankingRequest.id)
    }

    fun scheduleSyncRanking(ranking: Ranking): Flow<WorkInfo?> {
        val data =
            workDataOf(
                LOCAL_RANKING_ID to ranking.localId,
                REMOTE_RANKING_ID to ranking.remoteId,
                IS_ADMIN to ranking.isAdmin,
            )

        val syncRequest =
            OneTimeWorkRequestBuilder<SyncRankingWorker>()
                .setInputData(data)
                .setConstraints(WORK_MANAGER_DEFAULT_CONSTRAINTS)
                .addTag("$SYNC_REMOTE_RANKING${ranking.remoteId}")
                .build()

        val workName =
            "${SYNC_REMOTE_RANKING}_" +
                    "NAME:${ranking.name}_LOCALID:${ranking.localId}_REMOTEID:${ranking.remoteId}"

        workManager.enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            syncRequest,
        )

        return workManager.getWorkInfoByIdFlow(syncRequest.id)
    }

    fun scheduleRemoteRankDeletion(rankId: Long): Flow<WorkInfo?> {
        val data = workDataOf(REMOTE_RANKING_ID to rankId)

        val workName = "${DELETE_REMOTE_RANKING}_" + "RANK_ID_$rankId"

        val deleteRemoteRankRequest =
            OneTimeWorkRequestBuilder<DeleteRemoteRankingWorker>()
                .setInputData(data)
                .setConstraints(WORK_MANAGER_DEFAULT_CONSTRAINTS)
                .build()


        workManager.enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            deleteRemoteRankRequest,
        )

        return workManager.getWorkInfoByIdFlow(deleteRemoteRankRequest.id)
    }
}