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
    suspend fun updateRankingWithPlayers(rankingWithPlayers: RankingWithPlayers)
    suspend fun saveRankingWithPlayers(rankingWithPlayers: RankingWithPlayers): Long
    suspend fun updateRanking(ranking: RankingEntity)
    fun getPlayer(id: Long): Flow<PlayerEntity?>
    suspend fun insertPlayer(player: PlayerEntity): Long
    suspend fun insertPlayers(players: List<PlayerEntity>)
    suspend fun updatePlayer(player: PlayerEntity)
    suspend fun updatePlayers(players: List<PlayerEntity>)
    suspend fun deletePlayer(player: PlayerEntity)
    suspend fun deleteAllPlayers(rankingId: Long)
    suspend fun getPlayersByRanking(rankingId: Long): List<PlayerEntity>
    suspend fun deletePlayersNotInRanking(rankingId: Long, playerIds: List<Long>)
}