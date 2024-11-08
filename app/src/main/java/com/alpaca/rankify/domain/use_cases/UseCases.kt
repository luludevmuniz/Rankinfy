package com.alpaca.rankify.domain.use_cases

import com.alpaca.rankify.domain.use_cases.local.player.create_player.CreatePlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.player.delete_all_players.DeleteAllPlayersUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.create_ranking.CreateRankingUseCase
import com.alpaca.rankify.domain.use_cases.remote.ranking.create_remote_ranking.CreateRemoteRankingUseCase
import com.alpaca.rankify.domain.use_cases.local.player.delete_player.DeletePlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.delete_ranking.DeleteRankingUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.get_all_rankings.GetAllRankingsUseCase
import com.alpaca.rankify.domain.use_cases.local.player.get_player.GetPlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.get_ranking.GetRankingUseCase
import com.alpaca.rankify.domain.use_cases.remote.ranking.get_remote_ranking.GetRemoteRankingUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_rank_creation.ScheduleRemoteRankingCreationUseCase
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

data class UseCases(
    val createRanking: CreateRankingUseCase,
    val createRemoteRank: CreateRemoteRankingUseCase,
    val updateRanking: UpdateRankingUseCase,
    val updateRankWithPlayers: UpdateRankingWithPlayersUseCase,
    val getRanking: GetRankingUseCase,
    val getRankingWithRemoteId: GetRankingWithRemoteIdUseCase,
    val getRemoteRankingUseCase: GetRemoteRankingUseCase,
    val getAllRankings: GetAllRankingsUseCase,
    val deleteRanking: DeleteRankingUseCase,
    val deleteRemoteRanking: DeleteRemoteRankingUseCase,
    val searchRanking: SearchRankingUseCase,
    val getPlayer: GetPlayerUseCase,
    val createPlayer: CreatePlayerUseCase,
    val createRemotePlayer: CreateRemotePlayerUseCase,
    val updatePlayer: UpdatePlayerUseCase,
    val updateRemotePlayer: UpdateRemotePlayerUseCase,
    val deletePlayer: DeletePlayerUseCase,
    val deleteAllPlayers: DeleteAllPlayersUseCase,
    val deleteRemotePlayer: DeleteRemotePlayerUseCase,
    val scheduleRemoteRankingCreation: ScheduleRemoteRankingCreationUseCase,
    val scheduleSyncRanking: ScheduleSyncRankingUseCase,
    val scheduleRemotePlayerCreation: ScheduleRemotePlayerCreationUseCase,
    val scheduleRemotePlayerUpdateUseCase: ScheduleRemotePlayerUpdateUseCase,
    val scheduleRemotePlayerDeletion: ScheduleRemotePlayerDeletionUseCase,
    val scheduleRemoteRankingDeletion: ScheduleRemoteRankingDeletionUseCase,
)
