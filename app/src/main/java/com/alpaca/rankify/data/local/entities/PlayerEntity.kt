package com.alpaca.rankify.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.alpaca.rankify.util.constant.DatabaseConstants.PLAYER_TABLE

@Entity(
    tableName = PLAYER_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = RankingEntity::class,
            parentColumns = ["local_id"],
            childColumns = ["ranking_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["ranking_id"]), Index(value = ["remote_id"], unique = true)]
)
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    val localId: Long = 0,
    @ColumnInfo(name = "remote_id")
    val remoteId: Long? = null,
    @ColumnInfo(name = "ranking_id")
    val rankingId: Long,
    val name: String,
    val currentRankingPosition: Int = 0,
    val previousRankingPosition: Int = 0,
    val score: String
)