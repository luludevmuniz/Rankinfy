package com.alpaca.hyperpong.domain.use_cases.update_player

import com.alpaca.hyperpong.data.repository.Repository
import com.alpaca.hyperpong.domain.model.Player

class UpdatePlayerUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(player: Player) = repository.updatePlayer(player = player)
}