package com.alpaca.rankify.data.repository

import com.alpaca.rankify.data.remote.ApiService
import com.alpaca.rankify.data.remote.models.NetworkRanking
import com.alpaca.rankify.domain.model.CreatePlayerDTO
import com.alpaca.rankify.domain.model.CreateRankingDTO
import com.alpaca.rankify.domain.model.Player
import com.alpaca.rankify.domain.repository.RemoteDataSource

class RemoteDataSourceImpl(
    private val apiService: ApiService,
) : RemoteDataSource {
    override suspend fun createRanking(ranking: CreateRankingDTO): NetworkRanking = apiService.createRanking(ranking = ranking)

    override suspend fun getRanking(
        id: Long,
        password: String?,
    ): NetworkRanking =
        apiService.getRanking(
            id = id,
            password = password,
        )

    override suspend fun createPlayer(player: CreatePlayerDTO): Player = apiService.createPlayer(player = player)
}
