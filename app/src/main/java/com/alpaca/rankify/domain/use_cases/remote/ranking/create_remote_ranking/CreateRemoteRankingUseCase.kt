package com.alpaca.rankify.domain.use_cases.remote.ranking.create_remote_ranking

import com.alpaca.rankify.data.remote.models.NetworkRanking
import com.alpaca.rankify.data.repository.Repository
import com.alpaca.rankify.domain.model.CreateRankingDTO

class CreateRemoteRankingUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(ranking: CreateRankingDTO): NetworkRanking =
        repository.createRemoteRanking(ranking = ranking)
}