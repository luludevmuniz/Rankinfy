package com.alpaca.rankify.data.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.alpaca.rankify.domain.use_cases.UseCases

/**
 * A custom [WorkerFactory] for creating instances of [ListenableWorker] subclasses used in tests.
 *
 * This factory is responsible for instantiating the appropriate worker class based on the provided
 * `workerClassName`. It injects the necessary dependencies, such as [UseCases], into the worker instances.
 *
 * @param useCases An instance of [UseCases] providing access to the application's business logic.
 */
class TestWorkerFactory(private val useCases: UseCases): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            //Ranking
            CreateRemoteRankingWorker::class.java.name ->
                CreateRemoteRankingWorker(appContext, workerParameters, useCases)
            SyncRankingWorker::class.java.name ->
                SyncRankingWorker(appContext, workerParameters, useCases)
            DeleteRemoteRankingWorker::class.java.name ->
                DeleteRemoteRankingWorker(appContext, workerParameters, useCases)
            //Player
            CreateRemotePlayerWorker::class.java.name ->
                CreateRemotePlayerWorker(appContext, workerParameters, useCases)
            DeleteRemotePlayerWorker::class.java.name ->
                DeleteRemotePlayerWorker(appContext, workerParameters, useCases)
            UpdateRemotePlayerWorker::class.java.name ->
                UpdateRemotePlayerWorker(appContext, workerParameters, useCases)
            else -> null
        }
    }
}