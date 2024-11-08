package com.alpaca.rankify.data.repository

import com.alpaca.rankify.data.local.Database
import com.alpaca.rankify.data.local.entities.PlayerEntity
import com.alpaca.rankify.data.local.entities.RankingEntity
import com.alpaca.rankify.data.local.entities.RankingWithPlayers
import com.alpaca.rankify.domain.repository.LocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(database: Database) : LocalDataSource {
    private val rankingDao = database.rankingDao()
    private val playerDao = database.playerDao()

    override fun getRanking(id: Long): Flow<RankingWithPlayers?> =
        rankingDao.getRankingWithPlayers(id = id)

    override fun getRankingWithRemoteId(id: Long): Flow<RankingEntity?> =
        rankingDao.getRankingWithRemoteId(id = id)

    override fun getAllRankings(): Flow<List<RankingEntity>> = rankingDao.getAllRankings()

    override suspend fun saveRanking(ranking: RankingEntity) =
        rankingDao.saveRanking(ranking = ranking)

    override suspend fun deleteRanking(id: Long): Int =
        rankingDao.deleteRanking(id = id)

    override suspend fun updateRankingWithPlayers(rankingWithPlayers: RankingWithPlayers) {
        updateRanking(ranking = rankingWithPlayers.ranking)
        insertPlayers(players = rankingWithPlayers.players)
        deletePlayersNotInRanking(
            rankingId = rankingWithPlayers.ranking.localId,
            playerIds = rankingWithPlayers.players.map { it.remoteId ?: -1 }
        )
    }

    override suspend fun saveRankingWithPlayers(rankingWithPlayers: RankingWithPlayers): Long {
        val rankingId = saveRanking(ranking = rankingWithPlayers.ranking)
        if (rankingWithPlayers.players.isNotEmpty()) {
            val players = rankingWithPlayers.players.map { it.copy(rankingId = rankingId) }
            playerDao.insertPlayers(players = players)

        }
        return rankingId
    }

    override suspend fun updateRanking(ranking: RankingEntity) =
        rankingDao.updateRanking(ranking = ranking)

    override fun getPlayer(id: Long): Flow<PlayerEntity?> = playerDao.getPlayer(id = id)

    override suspend fun updatePlayers(players: List<PlayerEntity>) =
        playerDao.updatePlayers(players = players)

    override suspend fun insertPlayer(player: PlayerEntity): Long =
        playerDao.insertPlayer(player = player)

    override suspend fun insertPlayers(players: List<PlayerEntity>) =
        playerDao.insertPlayers(players = players)

    override suspend fun updatePlayer(player: PlayerEntity) =
        playerDao.updatePlayer(player = player)

    override suspend fun deletePlayer(player: PlayerEntity) =
        playerDao.deletePlayer(player = player)

    override suspend fun deleteAllPlayers(rankingId: Long) =
        playerDao.deleteAllPlayers(rankingId = rankingId)

    override suspend fun deletePlayersNotInRanking(rankingId: Long, playerIds: List<Long>) =
        playerDao.deletePlayersNotInRanking(rankingId = rankingId, playerIds = playerIds)

    override suspend fun getPlayersByRanking(rankingId: Long): List<PlayerEntity> =
        playerDao.getPlayersByRanking(rankingId = rankingId)
}