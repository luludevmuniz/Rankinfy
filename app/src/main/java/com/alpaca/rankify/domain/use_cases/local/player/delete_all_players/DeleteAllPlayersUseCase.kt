package com.alpaca.rankify.domain.use_cases.local.player.delete_all_players

import com.alpaca.rankify.data.repository.PlayerRepository

class DeleteAllPlayersUseCase(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(rankingId: Long) = repository.deleteAllPlayers(rankingId = rankingId)
}