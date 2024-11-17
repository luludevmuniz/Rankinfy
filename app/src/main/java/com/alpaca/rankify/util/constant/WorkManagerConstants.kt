package com.alpaca.rankify.util.constant

import androidx.work.Constraints
import androidx.work.NetworkType

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