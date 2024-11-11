package com.alpaca.rankify.util

import androidx.work.Constraints
import androidx.work.NetworkType

object NetworkConstants {
    const val BASE_URL = "https://com-alpaca-rankify-server.onrender.com"
    const val RANKING_ENDPOINT = "/ranking"
    const val PLAYER_ENDPOINT = "/player"
    const val PARAMETER_ID = "id"
    const val PARAMETER_PASSWORD = "password"
}

object DatabaseConstants {
    const val DATABASE_NAME = "pongrankings.db"
    const val RANKING_TABLE = "rankings"
    const val PLAYER_TABLE = "players"
}

object SecurityConstants {
    const val MIN_RANKING_PASSWORD_SIZE = 6
}

object WorkManagerConstants {
    object WorkData {
        const val LOCAL_RANKING_ID = "localId"
        const val REMOTE_RANKING_ID = "remoteId"
        const val ADMIN_PASSWORD = "adminPassword"
        const val PLAYER_ID = "playerId"
        const val PLAYER_NAME = "playerName"
        const val PLAYER_SCORE = "playerScore"
        const val IS_ADMIN = "isAdmin"
        const val MESSAGE = "message"
    }
    object UniqueWorkName {
        const val CREATE_REMOTE_RANKING = "CREATE_REMOTE_RANKING_WORK"
        const val SYNC_REMOTE_RANKING = "SYNC_REMOTE_RANKING_WORK"
        const val DELETE_REMOTE_RANKING = "DELETE_REMOTE_RANKING_WORK"
        const val CREATE_REMOTE_PLAYER = "CREATE_REMOTE_PLAYER_WORK"
        const val DELETE_REMOTE_PLAYER = "DELETE_REMOTE_PLAYER_WORK"
        const val UPDATE_REMOTE_PLAYER = "UPDATE_REMOTE_PLAYER_WORK"
    }
    val WORK_MANAGER_DEFAULT_CONSTRAINTS =
        Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
}

object TestingTags {
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
