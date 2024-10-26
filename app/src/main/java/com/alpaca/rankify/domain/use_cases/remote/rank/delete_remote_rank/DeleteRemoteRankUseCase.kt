package com.alpaca.rankify.domain.use_cases.remote.rank.delete_remote_rank

import com.alpaca.rankify.data.repository.Repository

class DeleteRemoteRankUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(id: Long) =
        repository.deleteRemoteRank(id = id)
}