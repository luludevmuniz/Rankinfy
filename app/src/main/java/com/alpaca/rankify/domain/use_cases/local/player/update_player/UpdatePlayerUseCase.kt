package com.alpaca.rankify.domain.use_cases.local.player.update_player

import com.alpaca.rankify.data.repository.Repository
import com.alpaca.rankify.domain.model.Player

class UpdatePlayerUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(player: Player) = repository.updatePlayer(player = player)
}