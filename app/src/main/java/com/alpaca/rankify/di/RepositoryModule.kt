package com.alpaca.rankify.di

import android.content.Context
import androidx.work.WorkManager
import com.alpaca.rankify.data.repository.Repository
import com.alpaca.rankify.domain.use_cases.UseCases
import com.alpaca.rankify.domain.use_cases.local.player.create_player.CreatePlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.player.delete_all_players.DeleteAllPlayersUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.create_ranking.CreateRankingUseCase
import com.alpaca.rankify.domain.use_cases.remote.ranking.create_remote_ranking.CreateRemoteRankingUseCase
import com.alpaca.rankify.domain.use_cases.local.player.delete_player.DeletePlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.delete_ranking.DeleteRankingUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.get_all_rankings.GetAllRankingsUseCase
import com.alpaca.rankify.domain.use_cases.local.player.get_player.GetPlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.get_ranking.GetRankingUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_rank_creation.ScheduleRemoteRankingCreationUseCase
import com.alpaca.rankify.domain.use_cases.remote.ranking.get_remote_ranking.GetRemoteRanking
import com.alpaca.rankify.domain.use_cases.work.schedule_sync_rank.ScheduleSyncRankingUseCase
import com.alpaca.rankify.domain.use_cases.remote.ranking.search_ranking.SearchRankingUseCase
import com.alpaca.rankify.domain.use_cases.local.player.update_player.UpdatePlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.get_ranking_with_remote_id.GetRankingWithRemoteIdUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.update_ranking.UpdateRankingUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.update_ranking_with_players.UpdateRankingWithPlayersUseCase
import com.alpaca.rankify.domain.use_cases.remote.player.create_remote_player.CreateRemotePlayerUseCase
import com.alpaca.rankify.domain.use_cases.remote.player.delete_remote_player.DeleteRemotePlayerUseCase
import com.alpaca.rankify.domain.use_cases.remote.player.update_remote_player.UpdateRemotePlayerUseCase
import com.alpaca.rankify.domain.use_cases.remote.ranking.delete_remote_ranking.DeleteRemoteRankingUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_player_creation.ScheduleRemotePlayerCreationUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_player_deletion.ScheduleRemotePlayerDeletionUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_player_update.ScheduleRemotePlayerUpdateUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_rank_deletion.ScheduleRemoteRankingDeletionUseCase
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
            createRanking = CreateRankingUseCase(repository = repository),
            createRemoteRank = CreateRemoteRankingUseCase(repository = repository),
            updateRanking = UpdateRankingUseCase(repository = repository),
            updateRankWithPlayers = UpdateRankingWithPlayersUseCase(repository = repository),
            deleteRanking = DeleteRankingUseCase(repository = repository),
            getRanking = GetRankingUseCase(repository = repository),
            getRankingWithRemoteId = GetRankingWithRemoteIdUseCase(repository = repository),
            getRemoteRanking = GetRemoteRanking(repository = repository),
            getAllRankings = GetAllRankingsUseCase(repository = repository),
            getPlayer = GetPlayerUseCase(repository = repository),
            deletePlayer = DeletePlayerUseCase(repository = repository),
            deleteAllPlayers = DeleteAllPlayersUseCase(repository = repository),
            deleteRemotePlayer = DeleteRemotePlayerUseCase(repository = repository),
            createPlayer = CreatePlayerUseCase(repository = repository),
            createRemotePlayer = CreateRemotePlayerUseCase(repository = repository),
            deleteRemoteRanking = DeleteRemoteRankingUseCase(repository = repository),
            updatePlayer = UpdatePlayerUseCase(repository = repository),
            updateRemotePlayer = UpdateRemotePlayerUseCase(repository = repository),
            searchRanking = SearchRankingUseCase(repository = repository),
            scheduleRemoteRankingCreation = ScheduleRemoteRankingCreationUseCase(repository = repository),
            scheduleRemoteRankingDeletion = ScheduleRemoteRankingDeletionUseCase(repository = repository),
            scheduleSyncRanking = ScheduleSyncRankingUseCase(repository = repository),
            scheduleRemotePlayerCreation = ScheduleRemotePlayerCreationUseCase(repository = repository),
            scheduleRemotePlayerUpdateUseCase = ScheduleRemotePlayerUpdateUseCase(repository = repository),
            scheduleRemotePlayerDeletion = ScheduleRemotePlayerDeletionUseCase(repository = repository),
        )
}