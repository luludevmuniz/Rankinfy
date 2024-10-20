package com.alpaca.rankify.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.alpaca.rankify.data.local.entities.PlayerEntity
import com.alpaca.rankify.util.Constants.PLAYER_TABLE

@Dao
interface PlayerDao {
    @Insert
    suspend fun insertPlayer(player: PlayerEntity): Long

    @Update
    suspend fun updatePlayer(player: PlayerEntity)

    @Delete
    suspend fun deletePlayer(player: PlayerEntity)

    @Query("SELECT * FROM $PLAYER_TABLE WHERE ranking_id = :rankingId")
    suspend fun getPlayersByRanking(rankingId: Long): List<PlayerEntity>
}