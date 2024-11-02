package com.alpaca.rankify.domain.use_cases.local.player.delete_all_players

import com.alpaca.rankify.data.repository.Repository

class DeleteAllPlayersUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(rankingId: Long) = repository.deleteAllPlayers(rankingId = rankingId)
}