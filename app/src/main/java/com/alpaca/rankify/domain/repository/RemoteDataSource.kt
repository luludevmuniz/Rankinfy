package com.alpaca.rankify.domain.repository

import com.alpaca.rankify.data.remote.models.NetworkRanking
import com.alpaca.rankify.domain.model.CreatePlayerDTO
import com.alpaca.rankify.domain.model.CreateRankingDTO
import com.alpaca.rankify.domain.model.Player
import com.alpaca.rankify.domain.model.UpdatePlayerDTO

interface RemoteDataSource {
    suspend fun createRanking(ranking: CreateRankingDTO): NetworkRanking

    suspend fun getRanking(
        id: Long,
        password: String? = null,
    ): NetworkRanking

    suspend fun deleteRanking(id: Long)

    suspend fun createPlayer(player: CreatePlayerDTO): Long

    suspend fun updatePlayer(player: UpdatePlayerDTO)

    suspend fun deletePlayer(id: Long)
}
