package com.alpaca.rankify.data.remote

import com.alpaca.rankify.data.remote.models.NetworkRanking
import com.alpaca.rankify.domain.model.CreatePlayerDTO
import com.alpaca.rankify.domain.model.CreateRankingDTO
import com.alpaca.rankify.domain.model.Player
import com.alpaca.rankify.util.Constants.PARAMETER_ID
import com.alpaca.rankify.util.Constants.PARAMETER_PASSWORD
import com.alpaca.rankify.util.Constants.PLAYERS_ENDPOINT
import com.alpaca.rankify.util.Constants.RANKING_ENDPOINT
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST(RANKING_ENDPOINT)
    suspend fun createRanking(
        @Body ranking: CreateRankingDTO,
    ): NetworkRanking

    @GET(RANKING_ENDPOINT)
    suspend fun getRanking(
        @Query(PARAMETER_ID) id: Long,
        @Query(PARAMETER_PASSWORD) password: String? = null,
    ): NetworkRanking

    @POST(PLAYERS_ENDPOINT)
    suspend fun createPlayer(
        @Body player: CreatePlayerDTO,
    ): Player
}
