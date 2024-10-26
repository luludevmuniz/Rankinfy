package com.alpaca.rankify.di

import android.content.Context
import androidx.work.WorkManager
import com.alpaca.rankify.data.repository.Repository
import com.alpaca.rankify.domain.use_cases.UseCases
import com.alpaca.rankify.domain.use_cases.work.cancel_remote_rank_creation.CancelRemoteRankCreationUseCase
import com.alpaca.rankify.domain.use_cases.work.cancel_sync_rank.CancelSyncRankUseCase
import com.alpaca.rankify.domain.use_cases.local.player.create_player.CreatePlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.create_ranking.CreateRankUseCase
import com.alpaca.rankify.domain.use_cases.remote.rank.create_remote_rank.CreateRemoteRankUseCase
import com.alpaca.rankify.domain.use_cases.local.player.delete_player.DeletePlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.delete_ranking.DeleteRankUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.get_all_rankings.GetAllRanksUseCase
import com.alpaca.rankify.domain.use_cases.local.player.get_player.GetPlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.get_ranking.GetRankUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_rank_creation.ScheduleRemoteRankCreationUseCase
import com.alpaca.rankify.domain.use_cases.remote.rank.get_remote_rank.GetRemoteRank
import com.alpaca.rankify.domain.use_cases.work.schedule_sync_rank.ScheduleSyncRankUseCase
import com.alpaca.rankify.domain.use_cases.remote.rank.search_rank.SearchRankUseCase
import com.alpaca.rankify.domain.use_cases.local.player.update_player.UpdatePlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.update_ranking.UpdateRankUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.update_ranking_with_players.UpdateRankWithPlayersUseCase
import com.alpaca.rankify.domain.use_cases.remote.player.create_remote_player.CreateRemotePlayerUseCase
import com.alpaca.rankify.domain.use_cases.remote.player.delete_remote_player.DeleteRemotePlayerUseCase
import com.alpaca.rankify.domain.use_cases.remote.player.update_remote_player.UpdateRemotePlayerUseCase
import com.alpaca.rankify.domain.use_cases.remote.rank.delete_remote_rank.DeleteRemoteRankUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_player_creation.ScheduleRemotePlayerCreationUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_player_deletion.ScheduleRemotePlayerDeletionUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_player_update.ScheduleRemotePlayerUpdateUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_rank_deletion.ScheduleRemoteRankDeletionUseCase
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
    @Singleton
    fun provideWorkManager(@ApplicationContext appContext: Context): WorkManager =
        WorkManager.getInstance(appContext)

    @Provides
    @Singleton
    fun provideUseCases(repository: Repository): UseCases =
        UseCases(
            createRank = CreateRankUseCase(repository = repository),
            createRemoteRank = CreateRemoteRankUseCase(repository = repository),
            updateRank = UpdateRankUseCase(repository = repository),
            updateRankWithPlayers = UpdateRankWithPlayersUseCase(repository = repository),
            deleteRank = DeleteRankUseCase(repository = repository),
            getRank = GetRankUseCase(repository = repository),
            getRemoteRank = GetRemoteRank(repository = repository),
            getAllRanks = GetAllRanksUseCase(repository = repository),
            getPlayer = GetPlayerUseCase(repository = repository),
            deletePlayer = DeletePlayerUseCase(repository = repository),
            deleteRemotePlayer = DeleteRemotePlayerUseCase(repository = repository),
            createPlayer = CreatePlayerUseCase(repository = repository),
            createRemotePlayer = CreateRemotePlayerUseCase(repository = repository),
            deleteRemoteRank = DeleteRemoteRankUseCase(repository = repository),
            updatePlayer = UpdatePlayerUseCase(repository = repository),
            updateRemotePlayer = UpdateRemotePlayerUseCase(repository = repository),
            searchRank = SearchRankUseCase(repository = repository),
            scheduleRemoteRankCreation = ScheduleRemoteRankCreationUseCase(repository = repository),
            scheduleRemoteRankDeletion = ScheduleRemoteRankDeletionUseCase(repository = repository),
            scheduleSyncRank = ScheduleSyncRankUseCase(repository = repository),
            scheduleRemotePlayerCreation = ScheduleRemotePlayerCreationUseCase(repository = repository),
            scheduleRemotePlayerUpdateUseCase = ScheduleRemotePlayerUpdateUseCase(repository = repository),
            scheduleRemotePlayerDeletion = ScheduleRemotePlayerDeletionUseCase(repository = repository),
            cancelRemoteRankCreation = CancelRemoteRankCreationUseCase(repository = repository),
            cancelSyncRank = CancelSyncRankUseCase(repository)
        )
}