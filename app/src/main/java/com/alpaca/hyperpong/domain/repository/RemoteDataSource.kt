package com.alpaca.hyperpong.domain.repository

import com.alpaca.hyperpong.data.remote.models.NetworkRanking
import com.alpaca.hyperpong.domain.model.CreatePlayerDTO
import com.alpaca.hyperpong.domain.model.CreateRankingDTO
import com.alpaca.hyperpong.domain.model.Player

interface RemoteDataSource {
    suspend fun createRanking(ranking: CreateRankingDTO): NetworkRanking
    suspend fun getRanking(
        name: String,
        id: Long,
        password: String? = null
    ): NetworkRanking
    suspend fun createPlayer(player: CreatePlayerDTO): Player
}