package com.alpaca.rankify.domain.use_cases.local.rank.get_all_rankings

import com.alpaca.rankify.data.repository.Repository
import com.alpaca.rankify.domain.model.Ranking
import kotlinx.coroutines.flow.Flow

class GetAllRankingsUseCase(
    private val repository: Repository
) {
    operator fun invoke(): Flow<List<Ranking>> = repository.getAllRankings()
}