package com.alpaca.rankify.domain.use_cases.remote.player.create_remote_player

import com.alpaca.rankify.data.repository.Repository
import com.alpaca.rankify.domain.model.CreatePlayerDTO

class CreateRemotePlayerUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(player: CreatePlayerDTO): Long =
        repository.createRemotePlayer(player = player)
}