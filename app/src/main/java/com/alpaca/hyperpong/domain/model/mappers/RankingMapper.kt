package com.alpaca.hyperpong.domain.model.mappers

import com.alpaca.hyperpong.data.local.entities.PlayerEntity
import com.alpaca.hyperpong.data.local.entities.RankingEntity
import com.alpaca.hyperpong.data.local.entities.RankingWithPlayers
import com.alpaca.hyperpong.data.remote.models.NetworkRanking
import com.alpaca.hyperpong.domain.model.Player
import com.alpaca.hyperpong.domain.model.Ranking

fun NetworkRanking.asEntity() = RankingEntity(
    localId = mobileId ?: 0L,
    name = name,
    lastUpdated = lastUpdated,
    isAdmin = isAdmin,
    remoteId = apiId
)

fun RankingEntity.asExternalModel() = Ranking(
    localId = localId,
    name = name,
    lastUpdated = lastUpdated,
    isAdmin = isAdmin,
    remoteId = remoteId
)

fun PlayerEntity.asExternalModel() = Player(
    id = localId,
    remoteId = remoteId,
    rankingId = rankingId,
    name = name,
    currentRankingPosition = currentRankingPosition,
    previousRankingPosition = previousRankingPosition,
    score = score
)

fun RankingWithPlayers.asExternalModel() = Ranking(
    localId = ranking.localId,
    name = ranking.name,
    lastUpdated = ranking.lastUpdated,
    isAdmin = ranking.isAdmin,
    remoteId = ranking.remoteId,
    players = players.map { it.asExternalModel() }
)

fun Player.asEntity() = PlayerEntity(
    localId = id,
    remoteId = remoteId,
    rankingId = rankingId,
    name = name,
    currentRankingPosition = currentRankingPosition,
    previousRankingPosition = previousRankingPosition,
    score = score
)