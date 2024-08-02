package com.alpaca.hyperpong.domain.use_cases.create_remote_ranking

import com.alpaca.hyperpong.data.remote.models.NetworkRanking
import com.alpaca.hyperpong.data.repository.Repository
import com.alpaca.hyperpong.domain.model.CreateRankingDTO

class CreateRemoteRankingUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(ranking: CreateRankingDTO): NetworkRanking =
        repository.createRemoteRanking(ranking = ranking)
}