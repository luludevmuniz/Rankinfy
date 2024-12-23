package com.alpaca.rankify.domain.use_cases.work.schedule_sync_rank

import com.alpaca.rankify.data.repository.RankingRepository
import com.alpaca.rankify.domain.model.Ranking

class ScheduleSyncRankingUseCase(
    private val repository: RankingRepository
) {
    operator fun invoke(ranking: Ranking) = repository.scheduleSyncRanking(ranking = ranking)
}