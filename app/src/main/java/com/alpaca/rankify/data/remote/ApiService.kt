package com.alpaca.rankify.data.remote

import com.alpaca.rankify.data.remote.models.NetworkRanking
import com.alpaca.rankify.domain.model.CreatePlayerDTO
import com.alpaca.rankify.domain.model.CreateRankingDTO
import com.alpaca.rankify.domain.model.UpdatePlayerDTO
import com.alpaca.rankify.util.constant.NetworkConstants.PARAMETER_ID
import com.alpaca.rankify.util.constant.NetworkConstants.PARAMETER_PASSWORD
import com.alpaca.rankify.util.constant.NetworkConstants.PLAYER_ENDPOINT
import com.alpaca.rankify.util.constant.NetworkConstants.RANKING_ENDPOINT
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @DELETE(RANKING_ENDPOINT)
    suspend fun deleteRanking(@Query(PARAMETER_ID) id: Long)

    @POST(PLAYER_ENDPOINT)
    suspend fun createPlayer(
        @Body player: CreatePlayerDTO,
    ): Long

    @PUT(PLAYER_ENDPOINT)
    suspend fun updatePlayer(@Body player: UpdatePlayerDTO)

    @DELETE(PLAYER_ENDPOINT)
    suspend fun deletePlayer(@Query(PARAMETER_ID) id: Long)
}