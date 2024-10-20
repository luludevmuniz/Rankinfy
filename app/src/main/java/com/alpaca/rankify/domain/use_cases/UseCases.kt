package com.alpaca.rankify.domain.use_cases

import com.alpaca.rankify.domain.use_cases.cancel_remote_ranking_creation.CancelRemoteRankingCreationUseCase
import com.alpaca.rankify.domain.use_cases.cancel_sync_ranking.CancelSyncRankingUseCase
import com.alpaca.rankify.domain.use_cases.create_player.CreatePlayerUseCase
import com.alpaca.rankify.domain.use_cases.create_ranking.CreateRakingUseCase
import com.alpaca.rankify.domain.use_cases.create_remote_ranking.CreateRemoteRankingUseCase
import com.alpaca.rankify.domain.use_cases.delete_player.DeletePlayerUseCase
import com.alpaca.rankify.domain.use_cases.delete_ranking.DeleteRankingUseCase
import com.alpaca.rankify.domain.use_cases.get_all_rankings.GetAllRankingsUseCase
import com.alpaca.rankify.domain.use_cases.get_ranking.GetRankingUseCase
import com.alpaca.rankify.domain.use_cases.get_remote_ranking.GetRemoteRanking
import com.alpaca.rankify.domain.use_cases.schedule_remote_ranking_creation.ScheduleRemoteRankingCreationUseCase
import com.alpaca.rankify.domain.use_cases.schedule_sync_ranking.ScheduleSyncRankingUseCase
import com.alpaca.rankify.domain.use_cases.search_ranking.SearchRankingUseCase
import com.alpaca.rankify.domain.use_cases.update_player.UpdatePlayerUseCase
import com.alpaca.rankify.domain.use_cases.update_ranking.UpdateRankingUseCase

data class UseCases(
    val createRanking: CreateRakingUseCase,
    val createRemoteRanking: CreateRemoteRankingUseCase,
    val updateRanking: UpdateRankingUseCase,
    val deletePlayer: DeletePlayerUseCase,
    val getRanking: GetRankingUseCase,
    val getRemoteRanking: GetRemoteRanking,
    val getAllRankings: GetAllRankingsUseCase,
    val createPlayer: CreatePlayerUseCase,
    val updatePlayer: UpdatePlayerUseCase,
    val deleteRanking: DeleteRankingUseCase,
    val searchRanking: SearchRankingUseCase,
    val scheduleRemoteRankingCreation: ScheduleRemoteRankingCreationUseCase,
    val scheduleSyncRanking: ScheduleSyncRankingUseCase,
    val cancelRemoteRankingCreation: CancelRemoteRankingCreationUseCase,
    val cancelSyncRanking: CancelSyncRankingUseCase,
)
