package com.alpaca.rankify.domain.use_cases.local.player.create_player

import com.alpaca.rankify.data.repository.Repository
import com.alpaca.rankify.domain.model.Player

class CreatePlayerUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(player: Player): Long = repository.createPlayer(player = player)
}