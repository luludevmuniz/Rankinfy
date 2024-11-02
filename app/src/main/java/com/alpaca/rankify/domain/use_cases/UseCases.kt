package com.alpaca.rankify.domain.use_cases

import com.alpaca.rankify.domain.use_cases.local.player.create_player.CreatePlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.player.delete_all_players.DeleteAllPlayersUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.create_ranking.CreateRankUseCase
import com.alpaca.rankify.domain.use_cases.remote.ranking.create_remote_ranking.CreateRemoteRankingUseCase
import com.alpaca.rankify.domain.use_cases.local.player.delete_player.DeletePlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.delete_ranking.DeleteRankUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.get_all_rankings.GetAllRanksUseCase
import com.alpaca.rankify.domain.use_cases.local.player.get_player.GetPlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.get_ranking.GetRankUseCase
import com.alpaca.rankify.domain.use_cases.remote.ranking.get_remote_ranking.GetRemoteRanking
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_rank_creation.ScheduleRemoteRankCreationUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_sync_rank.ScheduleSyncRankUseCase
import com.alpaca.rankify.domain.use_cases.remote.ranking.search_ranking.SearchRankingUseCase
import com.alpaca.rankify.domain.use_cases.local.player.update_player.UpdatePlayerUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.update_ranking.UpdateRankUseCase
import com.alpaca.rankify.domain.use_cases.local.rank.update_ranking_with_players.UpdateRankWithPlayersUseCase
import com.alpaca.rankify.domain.use_cases.remote.player.create_remote_player.CreateRemotePlayerUseCase
import com.alpaca.rankify.domain.use_cases.remote.player.delete_remote_player.DeleteRemotePlayerUseCase
import com.alpaca.rankify.domain.use_cases.remote.player.update_remote_player.UpdateRemotePlayerUseCase
import com.alpaca.rankify.domain.use_cases.remote.ranking.delete_remote_ranking.DeleteRemoteRankingUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_player_creation.ScheduleRemotePlayerCreationUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_player_deletion.ScheduleRemotePlayerDeletionUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_player_update.ScheduleRemotePlayerUpdateUseCase
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_rank_deletion.ScheduleRemoteRankDeletionUseCase

data class UseCases(
    val createRank: CreateRankUseCase,
    val createRemoteRank: CreateRemoteRankingUseCase,
    val updateRank: UpdateRankUseCase,
    val updateRankWithPlayers: UpdateRankWithPlayersUseCase,
    val getRank: GetRankUseCase,
    val getRemoteRanking: GetRemoteRanking,
    val getAllRanks: GetAllRanksUseCase,
    val deleteRank: DeleteRankUseCase,
    val deleteRemoteRank: DeleteRemoteRankingUseCase,
    val searchRank: SearchRankingUseCase,
    val getPlayer: GetPlayerUseCase,
    val createPlayer: CreatePlayerUseCase,
    val createRemotePlayer: CreateRemotePlayerUseCase,
    val updatePlayer: UpdatePlayerUseCase,
    val updateRemotePlayer: UpdateRemotePlayerUseCase,
    val deletePlayer: DeletePlayerUseCase,
    val deleteAllPlayers: DeleteAllPlayersUseCase,
    val deleteRemotePlayer: DeleteRemotePlayerUseCase,
    val scheduleRemoteRankCreation: ScheduleRemoteRankCreationUseCase,
    val scheduleSyncRank: ScheduleSyncRankUseCase,
    val scheduleRemotePlayerCreation: ScheduleRemotePlayerCreationUseCase,
    val scheduleRemotePlayerUpdateUseCase: ScheduleRemotePlayerUpdateUseCase,
    val scheduleRemotePlayerDeletion: ScheduleRemotePlayerDeletionUseCase,
    val scheduleRemoteRankDeletion: ScheduleRemoteRankDeletionUseCase,
)
