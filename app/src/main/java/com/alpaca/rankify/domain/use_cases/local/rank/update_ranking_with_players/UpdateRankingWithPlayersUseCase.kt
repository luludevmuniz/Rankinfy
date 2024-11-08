package com.alpaca.rankify.domain.use_cases.local.rank.update_ranking_with_players

import com.alpaca.rankify.data.repository.RankingRepository
import com.alpaca.rankify.domain.model.Ranking

class UpdateRankingWithPlayersUseCase(
    private val repository: RankingRepository,
) {
    suspend operator fun invoke(ranking: Ranking) = repository.updateRankingWithPlayers(ranking = ranking)
}
