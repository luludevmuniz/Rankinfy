package com.alpaca.rankify.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePlayerDTO(
    val name: String,
    val score: String,
    val id: Long
)
