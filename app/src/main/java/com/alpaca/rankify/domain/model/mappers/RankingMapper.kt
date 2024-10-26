package com.alpaca.rankify.domain.model.mappers

import com.alpaca.rankify.data.local.entities.PlayerEntity
import com.alpaca.rankify.data.local.entities.RankingEntity
import com.alpaca.rankify.data.local.entities.RankingWithPlayers
import com.alpaca.rankify.data.remote.models.NetworkRanking
import com.alpaca.rankify.domain.model.CreatePlayerDTO
import com.alpaca.rankify.domain.model.Player
import com.alpaca.rankify.domain.model.Ranking

fun NetworkRanking.asEntity(mobileId: Long? = null) = RankingEntity(
    localId = mobileId ?: 0L,
    name = name,
    lastUpdated = lastUpdated,
    isAdmin = isAdmin,
    remoteId = apiId
)

fun NetworkRanking.asExternalModel(
    mobileId: Long
) = Ranking(
    localId = mobileId,
    name = name,
    lastUpdated = lastUpdated,
    isAdmin = isAdmin,
    remoteId = apiId,
    players = players
)

fun Ranking.asEntity() = RankingEntity(
    localId = localId,
    name = name,
    lastUpdated = lastUpdated,
    isAdmin = isAdmin,
    remoteId = remoteId
)

fun RankingEntity.asExternalModel() = Ranking(
    localId = localId,
    name = name,
    lastUpdated = lastUpdated,
    isAdmin = isAdmin,
    remoteId = remoteId,
    adminPassword = adminPassword
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
    players = players.map { it.asExternalModel() },
    adminPassword = ranking.adminPassword
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

fun Player.asDto(remoteRankingId: Long? = null) = CreatePlayerDTO(
    name = name,
    score = score,
    remoteRankingId = remoteRankingId
)