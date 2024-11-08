package com.alpaca.rankify.domain.use_cases.remote.ranking.create_remote_ranking

import com.alpaca.rankify.data.remote.models.NetworkRanking
import com.alpaca.rankify.data.repository.RankingRepository
import com.alpaca.rankify.domain.model.CreateRankingDTO

class CreateRemoteRankingUseCase(
    private val repository: RankingRepository
) {
    suspend operator fun invoke(ranking: CreateRankingDTO): NetworkRanking =
        repository.createRemoteRanking(ranking = ranking)
}