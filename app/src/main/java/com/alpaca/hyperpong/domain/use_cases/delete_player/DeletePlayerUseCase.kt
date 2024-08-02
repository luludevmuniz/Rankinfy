package com.alpaca.hyperpong.domain.use_cases.delete_player

import com.alpaca.hyperpong.data.repository.Repository
import com.alpaca.hyperpong.domain.model.Player

class DeletePlayerUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(player: Player) = repository.deletePlayer(player = player)
}