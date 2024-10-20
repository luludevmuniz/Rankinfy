package com.alpaca.rankify.domain.use_cases.schedule_sync_ranking

import com.alpaca.rankify.data.repository.Repository
import com.alpaca.rankify.domain.model.Ranking

class ScheduleSyncRankingUseCase(
    private val repository: Repository
) {
    operator fun invoke(ranking: Ranking) = repository.scheduleSyncRanking(ranking = ranking)
}