package com.alpaca.rankify.domain.use_cases.local.player.update_player

import com.alpaca.rankify.data.repository.PlayerRepository
import com.alpaca.rankify.domain.model.Player

class UpdatePlayerUseCase(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(player: Player) = repository.updatePlayer(player = player)
}