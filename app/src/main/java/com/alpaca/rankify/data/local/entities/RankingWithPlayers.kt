package com.alpaca.rankify.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class RankingWithPlayers(
    @Embedded
    val ranking: RankingEntity,
    @Relation(
        parentColumn = "local_id",
        entityColumn = "ranking_id"
    )
    val players: List<PlayerEntity>
)