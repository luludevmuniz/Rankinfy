package com.alpaca.rankify.navigation

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.alpaca.rankify.R
import com.alpaca.rankify.util.TestingTags.Navigation.HOME_NAV_BUTTON
import com.alpaca.rankify.util.TestingTags.Navigation.MY_RANKINGS_NAV_BUTTON
import com.alpaca.rankify.util.TestingTags.Ranking.Tabs.CREATE_RANKING_TAB
import com.alpaca.rankify.util.TestingTags.Ranking.Tabs.SEARCH_RANKING_TAB
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
sealed class Panel {
    @Serializable
    data class RankingDetails(
        val id: Long,
        val adminPassword: String? = null
    ) : Panel()

    @Serializable
    data object Principal : Panel()
    data object CreateRanking : Panel()
    data object SearchRanking : Panel()
}

enum class AppDestinations(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int,
    val testTag: String,
) {
    HOME(
        label = R.string.home,
        icon = Icons.Default.Home,
        contentDescription = R.string.home,
        testTag = HOME_NAV_BUTTON
    ),
    RANKINGS(
        label = R.string.my_rankings,
        icon = Icons.Default.Favorite,
        contentDescription = R.string.my_rankings,
        testTag = MY_RANKINGS_NAV_BUTTON
    ),
}

enum class TabsDestinations(
    @StringRes val label: Int,
    val testTag: String
) {
    CREATE_RANKING(
        label = R.string.criar_ranking,
        testTag = CREATE_RANKING_TAB
    ),
    SEARCH_RANKING(
        label = R.string.buscar_ranking,
        testTag = SEARCH_RANKING_TAB
    )
}

@Parcelize
class RankingDestinationArgs(
    val id: Long,
    val adminPassword: String? = null
) : Parcelable