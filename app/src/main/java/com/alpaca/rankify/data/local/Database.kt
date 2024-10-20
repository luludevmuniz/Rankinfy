package com.alpaca.rankify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alpaca.rankify.data.local.dao.PlayerDao
import com.alpaca.rankify.data.local.dao.RankingDao
import com.alpaca.rankify.data.local.entities.PlayerEntity
import com.alpaca.rankify.data.local.entities.RankingEntity

@Database(
    entities = [RankingEntity::class, PlayerEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class Database: RoomDatabase() {
    abstract fun rankingDao(): RankingDao
    abstract fun playerDao(): PlayerDao
}