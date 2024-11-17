package com.alpaca.rankify.util.constant


object TestingTagsConstants {
    object Ranking {
        const val CREATE_RANKING_PANEL = "CREATE_RANKING_PANEL"
        const val SEARCH_RANKING_PANEL = "SEARCH_RANKING_PANEL"
        const val MY_RANKINGS_PANEL = "MY_RANKINGS_PANEL"
        const val RANKING_DETAILS_PANEL = "RANKING_DETAILS_PANEL"

        object Tabs {
            const val SEARCH_RANKING_TAB = "SEARCH_RANKING_TAB"
            const val CREATE_RANKING_TAB = "CREATE_RANKING_TAB"
        }
    }

    object Navigation {
        const val MY_RANKINGS_NAV_BUTTON = "MY_RANKINGS_NAV_BUTTON"
        const val HOME_NAV_BUTTON = "HOME_NAV_BUTTON"
    }

    object CreateRanking {
        const val RANKING_NAME_TEXT_FIELD = "RANKING_NAME_TEXT_FIELD"
        const val RANKING_PASSWORD_TEXT_FIELD = "RANKING_PASSWORD_TEXT_FIELD"
        const val CREATE_RANKING_BUTTON = "CREATE_RANKING_BUTTON"
    }

    object SearchRanking {
        const val RANKING_ID_TEXT_FIELD = "RANKING_ID_TEXT_FIELD"
        const val IS_ADMIN_CHECKBOX = "IS_ADMIN_CHECKBOX"
        const val RANKING_ADMIN_PASSWORD_TEXT_FIELD = "RANKING_ADMIN_PASSWORD_TEXT_FIELD"
        const val SEARCH_RANKING_BUTTON = "SEARCH_RANKING_BUTTON"
    }

    object SavedRankingItem {
        const val SAVED_RANKING_ITEM = "SAVED_RANKING_ITEM"
        const val RANKING_NOT_SAVED_TEXT = "RANKING_NOT_SAVED_TEXT"
        const val RANKING_SAVED_TEXT = "RANKING_SAVED_TEXT"
    }
}