package com.alpaca.hyperpong.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.alpaca.hyperpong.R
import kotlinx.serialization.Serializable

@Serializable
sealed class Panel {
    @Serializable
    data class RankingDetails(
        val id: Long,
        val adminPassword: String? = null
    ): Panel()
    @Serializable
    data object Principal: Panel()
    data object CreateRanking: Panel()
    data object SearchRanking: Panel()
}

enum class AppDestinations(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int
) {
    HOME(
        R.string.home,
        Icons.Default.Home,
        R.string.home
    ),
    RANKINGS(
        R.string.my_rankings,
        Icons.Default.Favorite,
        R.string.my_rankings
    ),
}