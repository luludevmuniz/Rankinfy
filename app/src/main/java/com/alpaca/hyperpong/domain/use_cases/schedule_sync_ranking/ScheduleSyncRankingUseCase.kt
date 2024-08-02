package com.alpaca.hyperpong.domain.use_cases.schedule_sync_ranking

import com.alpaca.hyperpong.data.repository.Repository
import com.alpaca.hyperpong.domain.model.Ranking

class ScheduleSyncRankingUseCase(
    private val repository: Repository
) {
    operator fun invoke(ranking: Ranking) = repository.scheduleSyncRanking(ranking = ranking)
}