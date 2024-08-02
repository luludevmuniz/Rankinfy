package com.alpaca.hyperpong.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: Long = 0,
    val rankingId: Long = 0,
    val remoteId: Long? = null,
    val name: String = "",
    val currentRankingPosition: Int = 0,
    val previousRankingPosition: Int = 0,
    val score: String = "",
)