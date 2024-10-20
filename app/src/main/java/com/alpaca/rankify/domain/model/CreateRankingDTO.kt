package com.alpaca.rankify.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateRankingDTO(
    val mobileId: Long? = null,
    val name: String,
    val adminPassword: String,
    val players: List<CreatePlayerDTO>? = emptyList()
)
