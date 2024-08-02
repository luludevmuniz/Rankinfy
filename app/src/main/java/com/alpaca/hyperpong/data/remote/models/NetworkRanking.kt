package com.alpaca.hyperpong.data.remote.models

import com.alpaca.hyperpong.domain.model.Player
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class NetworkRanking(
    val apiId: Long,
    val mobileId: Long? = null,
    val name: String,
    val lastUpdated: LocalDateTime,
    val players: List<Player> = emptyList(),
    val isAdmin: Boolean = false
)
