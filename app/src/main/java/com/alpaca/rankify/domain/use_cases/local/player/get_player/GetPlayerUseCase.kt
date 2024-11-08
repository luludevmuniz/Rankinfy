package com.alpaca.rankify.domain.use_cases.local.player.get_player

import com.alpaca.rankify.data.repository.PlayerRepository
import com.alpaca.rankify.domain.model.Player
import kotlinx.coroutines.flow.Flow

class GetPlayerUseCase(
    private val repository: PlayerRepository
) {
    operator fun invoke(id: Long): Flow<Player?> = repository.getPlayer(id = id)
}