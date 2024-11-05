package com.alpaca.rankify.navigation

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.alpaca.rankify.R
import kotlinx.parcelize.Parcelize
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

enum class TabsDestinations(@StringRes val label: Int) {
    CREATE_RANKING(R.string.criar_ranking),
    SEARCH_RANKING(R.string.buscar_ranking)
}

@Parcelize
class RankingDestination(
    val id: Long
) : Parcelable
