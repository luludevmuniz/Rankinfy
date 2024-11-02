package com.alpaca.rankify.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class NetworkPlayer(
    val id: Long = 0,
    val score: String = "",
    val name: String = "",
    val currentRankingPosition: Int = 0,
    val previousRankingPosition: Int = 0,
)
