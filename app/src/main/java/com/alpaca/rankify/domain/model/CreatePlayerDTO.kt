package com.alpaca.rankify.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlayerDTO(
    val remoteRankingId: Long? = null,
    val name: String,
    val score: String
)