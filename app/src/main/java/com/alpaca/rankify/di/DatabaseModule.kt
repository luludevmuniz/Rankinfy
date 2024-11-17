package com.alpaca.rankify.di

import android.content.Context
import androidx.room.Room
import com.alpaca.rankify.data.local.Database
import com.alpaca.rankify.data.repository.LocalDataSourceImpl
import com.alpaca.rankify.domain.repository.LocalDataSource
import com.alpaca.rankify.util.constant.DatabaseConstants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(database: Database): LocalDataSource =
        LocalDataSourceImpl(database = database)
}