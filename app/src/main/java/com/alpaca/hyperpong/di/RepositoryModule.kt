package com.alpaca.hyperpong.di

import android.content.Context
import androidx.work.WorkManager
import com.alpaca.hyperpong.data.repository.Repository
import com.alpaca.hyperpong.domain.use_cases.UseCases
import com.alpaca.hyperpong.domain.use_cases.cancel_remote_ranking_creation.CancelRemoteRankingCreationUseCase
import com.alpaca.hyperpong.domain.use_cases.cancel_sync_ranking.CancelSyncRankingUseCase
import com.alpaca.hyperpong.domain.use_cases.create_player.CreatePlayerUseCase
import com.alpaca.hyperpong.domain.use_cases.create_ranking.CreateRakingUseCase
import com.alpaca.hyperpong.domain.use_cases.create_remote_ranking.CreateRemoteRankingUseCase
import com.alpaca.hyperpong.domain.use_cases.delete_player.DeletePlayerUseCase
import com.alpaca.hyperpong.domain.use_cases.delete_ranking.DeleteRankingUseCase
import com.alpaca.hyperpong.domain.use_cases.get_all_rankings.GetAllRankingsUseCase
import com.alpaca.hyperpong.domain.use_cases.get_ranking.GetRankingUseCase
import com.alpaca.hyperpong.domain.use_cases.schedule_remote_ranking_creation.ScheduleRemoteRankingCreationUseCase
import com.alpaca.hyperpong.domain.use_cases.get_remote_ranking.GetRemoteRanking
import com.alpaca.hyperpong.domain.use_cases.schedule_sync_ranking.ScheduleSyncRankingUseCase
import com.alpaca.hyperpong.domain.use_cases.search_ranking.SearchRankingUseCase
import com.alpaca.hyperpong.domain.use_cases.update_player.UpdatePlayerUseCase
import com.alpaca.hyperpong.domain.use_cases.update_ranking.UpdateRankingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideWorkManager(@ApplicationContext appContext: Context): WorkManager =
        WorkManager.getInstance(appContext)

    @Provides
    @Singleton
    fun provideUseCases(repository: Repository): UseCases =
        UseCases(
            createRanking = CreateRakingUseCase(repository = repository),
            createRemoteRanking = CreateRemoteRankingUseCase(repository = repository),
            updateRanking = UpdateRankingUseCase(repository = repository),
            deletePlayer = DeletePlayerUseCase(repository = repository),
            getRanking = GetRankingUseCase(repository = repository),
            getRemoteRanking = GetRemoteRanking(repository = repository),
            getAllRankings = GetAllRankingsUseCase(repository = repository),
            createPlayer = CreatePlayerUseCase(repository = repository),
            deleteRanking = DeleteRankingUseCase(repository = repository),
            updatePlayer = UpdatePlayerUseCase(repository = repository),
            searchRanking = SearchRankingUseCase(repository = repository),
            scheduleRemoteRankingCreation = ScheduleRemoteRankingCreationUseCase(repository = repository),
            scheduleSyncRanking = ScheduleSyncRankingUseCase(repository = repository),
            cancelRemoteRankingCreation = CancelRemoteRankingCreationUseCase(repository = repository),
            cancelSyncRanking = CancelSyncRankingUseCase(repository)
        )
}