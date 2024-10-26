package com.alpaca.rankify.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.alpaca.rankify.data.local.entities.RankingEntity
import com.alpaca.rankify.data.local.entities.RankingWithPlayers
import com.alpaca.rankify.util.Constants.RANKING_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface RankingDao {

    @Transaction
    @Query("SELECT * FROM $RANKING_TABLE WHERE local_id = :id")
    fun getRankingWithPlayers(id: Long): Flow<RankingWithPlayers?>

    @Query("SELECT * FROM $RANKING_TABLE WHERE local_id = :id")
    fun getRanking(id: Long): Flow<RankingEntity>

    @Query("SELECT * FROM $RANKING_TABLE")
    fun getAllRankings(): Flow<List<RankingEntity>>

    @Insert
    suspend fun saveRanking(ranking: RankingEntity): Long

    @Query("DELETE FROM $RANKING_TABLE WHERE local_id = :id")
    suspend fun deleteRanking(id: Long): Int

//    @Update
//    suspend fun updateRankingWithPlayers(rankingWithPlayers: RankingWithPlayers)

    @Update
    suspend fun updateRanking(ranking: RankingEntity)
}