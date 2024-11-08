package com.alpaca.rankify.domain.use_cases.local.player.delete_player

import com.alpaca.rankify.data.repository.PlayerRepository
import com.alpaca.rankify.domain.model.Player

class DeletePlayerUseCase(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(player: Player) = repository.deletePlayer(player = player)
}