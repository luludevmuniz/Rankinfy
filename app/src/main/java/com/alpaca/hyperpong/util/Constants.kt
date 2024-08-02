package com.alpaca.hyperpong.util

object Constants {
    const val BASE_URL = "http://10.0.2.2:8080"
    const val RANKING_TABLE = "rankings"
    const val PLAYER_TABLE = "players"
    const val DATABASE_NAME = "pongrankings.db"
    const val MIN_RANKING_PASSWORD_SIZE = 6
    const val WORK_DATA_RANKING_NAME = "rankingName"
    const val WORK_DATA_LOCAL_RANKING_ID = "localId"
    const val WORK_DATA_REMOTE_RANKING_ID = "remoteId"
    const val WORK_DATA_ADMIN_PASSWORD = "adminPassword"
    const val WORK_DATA_IS_ADMIN = "isAdmin"
    const val UNIQUE_WORK_NAME_CREATE_REMOTE_RANKING = "CREATE_REMOTE_RANKING_WORK"
    const val UNIQUE_WORK_NAME_SYNC_REMOTE_RANKING = "SYNC_REMOTE_RANKING_WORK"
    const val BACKOFF_DELAY = 1L
    const val RANKING_ENDPOINT = "/ranking"
    const val PLAYERS_ENDPOINT = "/player"
    const val PARAMETER_NAME = "name"
    const val PARAMETER_ID = "id"
    const val PARAMETER_PASSWORD = "password"
}