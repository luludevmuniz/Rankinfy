package com.alpaca.hyperpong.domain.use_cases.create_player

import com.alpaca.hyperpong.data.repository.Repository
import com.alpaca.hyperpong.domain.model.Player

class CreatePlayerUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(player: Player) {
        repository.createPlayer(player = player)
    }
}