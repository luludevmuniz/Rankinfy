package com.alpaca.rankify.domain.repository

import com.alpaca.rankify.data.local.entities.PlayerEntity
import com.alpaca.rankify.data.local.entities.RankingEntity
import com.alpaca.rankify.data.local.entities.RankingWithPlayers
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getRanking(id: Long): Flow<RankingWithPlayers?>
    fun getAllRankings(): Flow<List<RankingEntity>>
    suspend fun saveRanking(ranking: RankingEntity): Long
    suspend fun deleteRanking(id: Long): Int
    suspend fun updateRanking(ranking: RankingEntity)
    suspend fun insertPlayer(player: PlayerEntity): Long
    suspend fun updatePlayer(player: PlayerEntity)
    suspend fun deletePlayer(player: PlayerEntity)
    suspend fun getPlayersByRanking(rankingId: Long): List<PlayerEntity>
}