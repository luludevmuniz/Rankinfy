package com.alpaca.rankify.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Ranking(
    val localId: Long = 0L,
    val remoteId: Long? = null,
    val name: String = "",
    val lastUpdated: LocalDateTime? = null,
    val players: ImmutableList<Player> = persistentListOf(),
    val adminPassword: String? = null,
    val isAdmin: Boolean = false
) {
    val sortedPlayers get() = players.sortedBy { it.currentRankingPosition }.toImmutableList()
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