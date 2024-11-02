package com.alpaca.rankify.domain.use_cases.remote.ranking.delete_remote_ranking

import com.alpaca.rankify.data.repository.Repository

class DeleteRemoteRankingUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(id: Long) =
        repository.deleteRemoteRank(id = id)
}