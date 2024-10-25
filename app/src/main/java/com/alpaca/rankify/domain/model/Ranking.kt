package com.alpaca.rankify.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.serialization.Serializable

@Serializable
data class Ranking(
    val localId: Long = 0L,
    val remoteId: Long? = null,
    val name: String = "",
    val lastUpdated: LocalDateTime? = null,
    val players: List<Player> = emptyList(),
    val adminPassword: String? = null,
    val isAdmin: Boolean = false
) {
    val sortedPlayers get() = players.sortedBy { it.currentRankingPosition }
    val formattedLastUpdated: String
        get() = lastUpdated?.format(
            LocalDateTime.Format {
                date(
                    LocalDate.Format {
                        dayOfMonth(padding = Padding.ZERO)
                        char('/')
                        monthNumber(padding = Padding.ZERO)
                        char('/')
                        year()
                    }
                )
                char(' ')
                time(
                    LocalTime.Format {
                        hour(); char(':'); minute()
                    }
                )
            }
        ).orEmpty()
}