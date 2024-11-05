package com.alpaca.rankify.domain.model

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
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