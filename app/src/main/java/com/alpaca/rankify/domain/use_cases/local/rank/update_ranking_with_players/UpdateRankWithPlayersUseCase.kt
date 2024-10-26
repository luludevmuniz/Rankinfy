package com.alpaca.rankify.domain.use_cases.local.rank.update_ranking_with_players

import com.alpaca.rankify.data.repository.Repository
import com.alpaca.rankify.domain.model.Ranking

class UpdateRankWithPlayersUseCase(
    private val repository: Repository,
) {
    suspend operator fun invoke(ranking: Ranking) = repository.updateRankingWithPlayers(ranking = ranking)
}
