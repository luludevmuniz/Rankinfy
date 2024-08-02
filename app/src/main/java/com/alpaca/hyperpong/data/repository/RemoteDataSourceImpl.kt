package com.alpaca.hyperpong.data.repository

import com.alpaca.hyperpong.data.remote.ApiService
import com.alpaca.hyperpong.data.remote.models.NetworkRanking
import com.alpaca.hyperpong.domain.model.CreatePlayerDTO
import com.alpaca.hyperpong.domain.model.CreateRankingDTO
import com.alpaca.hyperpong.domain.model.Player
import com.alpaca.hyperpong.domain.repository.RemoteDataSource

class RemoteDataSourceImpl(private val apiService: ApiService) : RemoteDataSource {
    override suspend fun createRanking(ranking: CreateRankingDTO): NetworkRanking =
        apiService.createRanking(ranking = ranking)


    override suspend fun getRanking(
        name: String,
        id: Long,
        password: String?
    ): NetworkRanking =
        apiService.getRanking(
            name = name,
            id = id,
            password = password
        )

    override suspend fun createPlayer(player: CreatePlayerDTO): Player =
        apiService.createPlayer(player = player)
}