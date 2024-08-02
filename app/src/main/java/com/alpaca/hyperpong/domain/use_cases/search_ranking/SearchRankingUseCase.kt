package com.alpaca.hyperpong.domain.use_cases.search_ranking

import com.alpaca.hyperpong.data.repository.Repository

class SearchRankingUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(
        name: String,
        id: Long
    ): Long = repository.searchRanking(
        name = name,
        id = id
    )
}