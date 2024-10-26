package com.alpaca.rankify.data.repository

import com.alpaca.rankify.data.remote.ApiService
import com.alpaca.rankify.data.remote.models.NetworkRanking
import com.alpaca.rankify.domain.model.CreatePlayerDTO
import com.alpaca.rankify.domain.model.CreateRankingDTO
import com.alpaca.rankify.domain.model.Player
import com.alpaca.rankify.domain.model.UpdatePlayerDTO
import com.alpaca.rankify.domain.repository.RemoteDataSource

class RemoteDataSourceImpl(
    private val apiService: ApiService,
) : RemoteDataSource {
    override suspend fun createRanking(ranking: CreateRankingDTO): NetworkRanking =
        apiService.createRanking(ranking = ranking)

    override suspend fun getRanking(
        id: Long,
        password: String?,
    ): NetworkRanking =
        apiService.getRanking(
            id = id,
            password = password,
        )

    override suspend fun deleteRanking(id: Long) = apiService.deleteRanking(id = id)


    override suspend fun createPlayer(player: CreatePlayerDTO): Long =
        apiService.createPlayer(player = player)

    override suspend fun updatePlayer(player: UpdatePlayerDTO) = apiService.updatePlayer(player = player)

    override suspend fun deletePlayer(id: Long) = apiService.deletePlayer(id = id)
}
