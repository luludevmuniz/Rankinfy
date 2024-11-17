package com.alpaca.rankify.navigation

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.alpaca.rankify.R
import com.alpaca.rankify.util.constant.TestingTagsConstants.Navigation.HOME_NAV_BUTTON
import com.alpaca.rankify.util.constant.TestingTagsConstants.Navigation.MY_RANKINGS_NAV_BUTTON
import com.alpaca.rankify.util.constant.TestingTagsConstants.Ranking.Tabs.CREATE_RANKING_TAB
import com.alpaca.rankify.util.constant.TestingTagsConstants.Ranking.Tabs.SEARCH_RANKING_TAB
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
    @DrawableRes val icon: Int,
    @StringRes val contentDescription: Int,
    val testTag: String,
) {
    HOME(
        label = R.string.home,
        icon = R.drawable.ic_home,
        contentDescription = R.string.home,
        testTag = HOME_NAV_BUTTON
    ),
    RANKINGS(
        label = R.string.my_rankings,
        icon = R.drawable.ic_favorite,
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