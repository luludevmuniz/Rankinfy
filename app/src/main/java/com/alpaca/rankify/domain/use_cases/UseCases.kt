package com.alpaca.rankify.domain.use_cases

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
import com.alpaca.rankify.domain.use_cases.remote.rank.get_remote_rank.GetRemoteRank
import com.alpaca.rankify.domain.use_cases.work.schedule_remote_rank_creation.ScheduleRemoteRankCreationUseCase
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

data class UseCases(
    val createRank: CreateRankUseCase,
    val createRemoteRank: CreateRemoteRankUseCase,
    val updateRank: UpdateRankUseCase,
    val updateRankWithPlayers: UpdateRankWithPlayersUseCase,
    val getRank: GetRankUseCase,
    val getRemoteRank: GetRemoteRank,
    val getAllRanks: GetAllRanksUseCase,
    val deleteRank: DeleteRankUseCase,
    val deleteRemoteRank: DeleteRemoteRankUseCase,
    val searchRank: SearchRankUseCase,
    val getPlayer: GetPlayerUseCase,
    val createPlayer: CreatePlayerUseCase,
    val createRemotePlayer: CreateRemotePlayerUseCase,
    val updatePlayer: UpdatePlayerUseCase,
    val updateRemotePlayer: UpdateRemotePlayerUseCase,
    val deletePlayer: DeletePlayerUseCase,
    val deleteRemotePlayer: DeleteRemotePlayerUseCase,
    val scheduleRemoteRankCreation: ScheduleRemoteRankCreationUseCase,
    val scheduleSyncRank: ScheduleSyncRankUseCase,
    val scheduleRemotePlayerCreation: ScheduleRemotePlayerCreationUseCase,
    val scheduleRemotePlayerUpdateUseCase: ScheduleRemotePlayerUpdateUseCase,
    val scheduleRemotePlayerDeletion: ScheduleRemotePlayerDeletionUseCase,
    val scheduleRemoteRankDeletion: ScheduleRemoteRankDeletionUseCase,
    val cancelRemoteRankCreation: CancelRemoteRankCreationUseCase,
    val cancelSyncRank: CancelSyncRankUseCase,
)
