package com.alpaca.rankify

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.alpaca.rankify.data.worker.CreateRemoteRankingWorker
import com.alpaca.rankify.domain.use_cases.UseCases
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: SyncRankingWorkerFactory

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface HiltWorkerFactoryEntryPoint {
        fun workerFactory(): HiltWorkerFactory
    }
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(EntryPoints.get(this, HiltWorkerFactoryEntryPoint::class.java).workerFactory())
            .build()
}

class SyncRankingWorkerFactory @Inject constructor(private val useCases: UseCases): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = CreateRemoteRankingWorker(
        appContext = appContext,
        workerParams = workerParameters,
        useCases = useCases
    )
}