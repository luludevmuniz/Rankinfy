package com.alpaca.rankify.data.remote.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkRanking(
    @SerialName("id")
    val apiId: Long,
    val name: String,
    val lastUpdated: LocalDateTime,
    val players: List<NetworkPlayer> = emptyList(),
    val isAdmin: Boolean = false
)
