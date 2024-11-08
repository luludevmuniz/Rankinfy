package com.alpaca.rankify.domain.use_cases.remote.player.update_remote_player

import com.alpaca.rankify.data.repository.PlayerRepository
import com.alpaca.rankify.domain.model.UpdatePlayerDTO

class UpdateRemotePlayerUseCase(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(player: UpdatePlayerDTO) =
        repository.updateRemotePlayer(player = player)
}