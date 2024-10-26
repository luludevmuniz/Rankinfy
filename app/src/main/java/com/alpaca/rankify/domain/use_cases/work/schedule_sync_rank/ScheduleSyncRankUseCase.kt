package com.alpaca.rankify.domain.use_cases.work.schedule_sync_rank

import com.alpaca.rankify.data.repository.Repository
import com.alpaca.rankify.domain.model.Ranking

class ScheduleSyncRankUseCase(
    private val repository: Repository
) {
    operator fun invoke(ranking: Ranking) = repository.scheduleSyncRanking(ranking = ranking)
}