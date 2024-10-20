package com.alpaca.rankify.data.remote.models

import com.alpaca.rankify.domain.model.Player
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkRanking(
    @SerialName("id")
    val apiId: Long,
    val name: String,
    val lastUpdated: LocalDateTime,
    val players: List<Player> = emptyList(),
    val isAdmin: Boolean = false
)
