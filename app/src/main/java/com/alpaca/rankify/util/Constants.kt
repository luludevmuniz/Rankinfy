package com.alpaca.rankify.util

import androidx.work.Constraints
import androidx.work.NetworkType

object Constants {
    const val BASE_URL = "https://com-alpaca-rankify-server.onrender.com"
    const val RANKING_TABLE = "rankings"
    const val PLAYER_TABLE = "players"
    const val DATABASE_NAME = "pongrankings.db"
    const val MIN_RANKING_PASSWORD_SIZE = 6
    //API
    const val RANKING_ENDPOINT = "/ranking"
    const val PLAYER_ENDPOINT = "/player"
    const val PARAMETER_ID = "id"
    const val PARAMETER_PASSWORD = "password"
    //WORKMANAGER
    const val WORK_DATA_LOCAL_RANKING_ID = "localId"
    const val WORK_DATA_REMOTE_RANK_ID = "remoteId"
    const val WORK_DATA_ADMIN_PASSWORD = "adminPassword"
    const val WORK_DATA_PLAYER_ID = "playerId"
    const val WORK_DATA_PLAYER_NAME = "playerName"
    const val WORK_DATA_PLAYER_SCORE = "playerScore"
    const val WORK_DATA_IS_ADMIN = "isAdmin"
    const val UNIQUE_WORK_NAME_CREATE_REMOTE_RANKING = "CREATE_REMOTE_RANKING_WORK"
    const val UNIQUE_WORK_NAME_SYNC_REMOTE_RANKING = "SYNC_REMOTE_RANKING_WORK"
    const val UNIQUE_WORK_NAME_DELETE_REMOTE_RANKING = "DELETE_REMOTE_RANKING_WORK"
    const val UNIQUE_WORK_NAME_CREATE_REMOTE_PLAYER = "CREATE_REMOTE_PLAYER_WORK"
    const val UNIQUE_WORK_NAME_DELETE_REMOTE_PLAYER = "DELETE_REMOTE_PLAYER_WORK"
    const val UNIQUE_WORK_NAME_UPDATE_REMOTE_PLAYER = "UPDATE_REMOTE_PLAYER_WORK"
    const val WORK_DATA_MESSAGE = "message"
    val WORK_MANAGER_DEFAULT_CONSTRAINTS =
        Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
}
