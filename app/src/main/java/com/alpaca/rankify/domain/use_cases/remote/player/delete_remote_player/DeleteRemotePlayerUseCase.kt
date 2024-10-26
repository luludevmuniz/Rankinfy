package com.alpaca.rankify.domain.use_cases.remote.player.delete_remote_player

import com.alpaca.rankify.data.repository.Repository

class DeleteRemotePlayerUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(id: Long) =
        repository.deleteRemotePlayer(id = id)
}