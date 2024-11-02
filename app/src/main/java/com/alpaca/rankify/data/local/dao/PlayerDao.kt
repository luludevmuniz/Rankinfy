package com.alpaca.rankify.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.alpaca.rankify.data.local.entities.PlayerEntity
import com.alpaca.rankify.util.Constants.PLAYER_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM $PLAYER_TABLE WHERE local_id = :id")
    fun getPlayer(id: Long): Flow<PlayerEntity?>

    @Insert
    suspend fun insertPlayer(player: PlayerEntity): Long

    @Upsert
    suspend fun insertPlayers(players: List<PlayerEntity>)

    @Update
    suspend fun updatePlayer(player: PlayerEntity)

    @Update
    suspend fun updatePlayers(players: List<PlayerEntity>)

    @Delete
    suspend fun deletePlayer(player: PlayerEntity)

    @Query("DELETE FROM $PLAYER_TABLE WHERE ranking_id = :rankingId")
    suspend fun deleteAllPlayers(rankingId: Long)

    @Query("SELECT * FROM $PLAYER_TABLE WHERE ranking_id = :rankingId")
    suspend fun getPlayersByRanking(rankingId: Long): List<PlayerEntity>

    @Query("DELETE FROM $PLAYER_TABLE WHERE ranking_id = :rankingId AND remote_id NOT IN (:playerIds)")
    suspend fun deletePlayersNotInRanking(rankingId: Long, playerIds: List<Long>)
}