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

    override fun getRanking(id: Long): Flow<RankingWithPlayers?> = rankingDao.getRankingWithPlayers(id = id)

    override fun getAllRankings(): Flow<List<RankingEntity>> = rankingDao.getAllRankings()

    override suspend fun saveRanking(ranking: RankingEntity) =
        rankingDao.saveRanking(ranking = ranking)

    override suspend fun deleteRanking(id: Long): Int =
        rankingDao.deleteRanking(id = id)

    override suspend fun updateRanking(ranking: RankingEntity) =
        rankingDao.updateRanking(ranking = ranking)

    override suspend fun insertPlayer(player: PlayerEntity): Long =
        playerDao.insertPlayer(player = player)

    override suspend fun updatePlayer(player: PlayerEntity) =
        playerDao.updatePlayer(player = player)

    override suspend fun deletePlayer(player: PlayerEntity) =
        playerDao.deletePlayer(player = player)

    override suspend fun getPlayersByRanking(rankingId: Long): List<PlayerEntity> =
        playerDao.getPlayersByRanking(rankingId = rankingId)
}