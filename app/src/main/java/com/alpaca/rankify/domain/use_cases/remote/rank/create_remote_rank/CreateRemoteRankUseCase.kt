package com.alpaca.rankify.domain.use_cases.remote.rank.create_remote_rank

import com.alpaca.rankify.data.remote.models.NetworkRanking
import com.alpaca.rankify.data.repository.Repository
import com.alpaca.rankify.domain.model.CreateRankingDTO

class CreateRemoteRankUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(ranking: CreateRankingDTO): NetworkRanking =
        repository.createRemoteRanking(ranking = ranking)
}