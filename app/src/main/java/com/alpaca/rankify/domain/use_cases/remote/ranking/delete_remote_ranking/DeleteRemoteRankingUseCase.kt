package com.alpaca.rankify.domain.use_cases.remote.ranking.delete_remote_ranking

import com.alpaca.rankify.data.repository.RankingRepository

class DeleteRemoteRankingUseCase(
    private val repository: RankingRepository
) {
    suspend operator fun invoke(id: Long) =
        repository.deleteRemoteRanking(id = id)
}