package com.alpaca.hyperpong.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateRankingDTO(
    val mobileId: Long,
    val name: String,
    val adminPassword: String,
    val players: List<CreatePlayerDTO>? = emptyList()
)
