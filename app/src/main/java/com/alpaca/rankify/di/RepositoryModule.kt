package com.alpaca.rankify.di

import android.content.Context
import androidx.work.WorkManager
import com.alpaca.rankify.data.repository.PlayerRepository
import com.alpaca.rankify.data.repository.RankingRepository
import com.alpaca.rankify.domain.use_cases.UseCases
import com.alpaca.rankify.domain.use_cases.local.player.create_player.CreatePlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.player.delete_all_players.DeleteAllPlayersUseCase
import com.alpaca.rankify.domain.use_cases.local.player.delete_player.DeletePlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.player.get_player.GetPlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.player.update_player.UpdatePlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.create_ranking.CreateRankingUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.delete_ranking.DeleteRankingUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.get_all_rankings.GetAllRankingsUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.get_ranking.GetRankingUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.get_ranking_with_remote_id.GetRankingWithRemoteIdUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.update_ranking.UpdateRankingUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.update_ranking_with_players.UpdateRankingWithPlayersUseCase
import com.alpaca.rankify.domain.use_cases.remote.player.create_remote_player.CreateRemotePlayerUseCase
import com.alpaca.rankify.domain.use_cases.remote.player.delete_remote_player.DeleteRemotePlayerUseCase
import com.alpaca.rankify.domain.use_cases.remote.player.update_remote_player.UpdateRemotePlayerUseCase
import com.alpaca.rankify.domain.use_cases.remote.ranking.create_remote_ranking.CreateRemoteRankingUseCase
import com.alpaca.rankify.domain.use_cases.remote.ranking.delete_remote_ranking.DeleteRemoteRankingUseCase
import com.alpaca.rankify.domain.use_cases.remote.ranking.get_remote_ranking.GetRemoteRankingUseCase
import com.alpaca.rankify.domain.use_cases.remote.ranking.search_ranking.SearchRankingUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_player_creation.ScheduleRemotePlayerCreationUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_player_deletion.ScheduleRemotePlayerDeletionUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_player_update.ScheduleRemotePlayerUpdateUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_rank_creation.ScheduleRemoteRankingCreationUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_rank_deletion.ScheduleRemoteRankingDeletionUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_sync_rank.ScheduleSyncRankingUseCase
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
    fun provideUseCases(
        rankingRepository: RankingRepository,
        playerRepository: PlayerRepository
    ): UseCases =
        UseCases(
            createRanking = CreateRankingUseCase(repository = rankingRepository),
            createRemoteRanking = CreateRemoteRankingUseCase(repository = rankingRepository),
            updateRanking = UpdateRankingUseCase(repository = rankingRepository),
            updateRankingWithPlayers = UpdateRankingWithPlayersUseCase(repository = rankingRepository),
            deleteRanking = DeleteRankingUseCase(repository = rankingRepository),
            getRanking = GetRankingUseCase(repository = rankingRepository),
            getRankingWithRemoteId = GetRankingWithRemoteIdUseCase(repository = rankingRepository),
            getRemoteRankingUseCase = GetRemoteRankingUseCase(repository = rankingRepository),
            getAllRankings = GetAllRankingsUseCase(repository = rankingRepository),
            deleteRemoteRanking = DeleteRemoteRankingUseCase(repository = rankingRepository),
            searchRanking = SearchRankingUseCase(repository = rankingRepository),
            scheduleRemoteRankingCreation = ScheduleRemoteRankingCreationUseCase(repository = rankingRepository),
            scheduleRemoteRankingDeletion = ScheduleRemoteRankingDeletionUseCase(repository = rankingRepository),
            scheduleSyncRanking = ScheduleSyncRankingUseCase(repository = rankingRepository),
            getPlayer = GetPlayerUseCase(repository = playerRepository),
            deletePlayer = DeletePlayerUseCase(repository = playerRepository),
            deleteAllPlayers = DeleteAllPlayersUseCase(repository = playerRepository),
            deleteRemotePlayer = DeleteRemotePlayerUseCase(repository = playerRepository),
            createPlayer = CreatePlayerUseCase(repository = playerRepository),
            createRemotePlayer = CreateRemotePlayerUseCase(repository = playerRepository),
            updatePlayer = UpdatePlayerUseCase(repository = playerRepository),
            updateRemotePlayer = UpdateRemotePlayerUseCase(repository = playerRepository),
            scheduleRemotePlayerCreation = ScheduleRemotePlayerCreationUseCase(repository = playerRepository),
            scheduleRemotePlayerUpdateUseCase = ScheduleRemotePlayerUpdateUseCase(repository = playerRepository),
            scheduleRemotePlayerDeletion = ScheduleRemotePlayerDeletionUseCase(repository = playerRepository),
        )
}