package com.alpaca.rankify.domain.use_cases.search_ranking

import com.alpaca.rankify.data.repository.Repository

class SearchRankingUseCase(
    private val repository: Repository,
) {
    suspend operator fun invoke(id: Long): Long = repository.searchRanking(id = id)
}
