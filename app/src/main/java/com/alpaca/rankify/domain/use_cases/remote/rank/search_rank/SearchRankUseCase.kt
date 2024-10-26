package com.alpaca.rankify.domain.use_cases.remote.rank.search_rank

import com.alpaca.rankify.data.repository.Repository

class SearchRankUseCase(
    private val repository: Repository,
) {
    suspend operator fun invoke(id: Long): Long = repository.searchRanking(id = id)
}
