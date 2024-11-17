package com.alpaca.rankify.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.alpaca.rankify.data.local.Converters
import com.alpaca.rankify.util.constant.DatabaseConstants.RANKING_TABLE
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = RANKING_TABLE)
@TypeConverters(Converters::class)
data class RankingEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    val localId: Long = 0,
    @ColumnInfo(name = "remote_id")
    val remoteId: Long? = null,
    val name: String,
    @ColumnInfo(name = "last_updated")
    val lastUpdated: LocalDateTime? = null,
    @ColumnInfo(name = "is_admin")
    val isAdmin: Boolean,
    @ColumnInfo(name = "admin_password")
    val adminPassword: String? = null,
)