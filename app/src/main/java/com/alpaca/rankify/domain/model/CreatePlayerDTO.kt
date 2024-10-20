package com.alpaca.rankify.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlayerDTO(
    val rankingId: Long,
    val name: String,
    val score: String
)