package com.alpaca.rankify.data.worker

import android.accounts.NetworkErrorException
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import com.alpaca.rankify.R
import com.alpaca.rankify.data.remote.models.NetworkRanking
import com.alpaca.rankify.domain.use_cases.UseCases
import com.alpaca.rankify.util.WorkManagerConstants
import com.alpaca.rankify.util.WorkManagerConstants.WorkData.MESSAGE
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.net.SocketTimeoutException

/**
 * Instrumented tests for [SyncRankingWorker].
 *
 * This class uses AndroidX Test to run tests on an Android device or emulator.
 * It verifies the worker's behavior under various conditions, including successful
 * synchronization, network errors, and unexpected exceptions.
 */
@RunWith(AndroidJUnit4::class)
class SyncRankingWorkerTest {
    private lateinit var context: Context

    companion object {
        private const val LOCAL_RANKING_ID = 1L
        private const val REMOTE_RANKING_ID = 2L
        private const val IS_ADMIN = true
    }

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    /**
     * Tests the [SyncRankingWorker] when the synchronization is successful.
     *
     * **Scenario:**
     * - Given a [SyncRankingWorker] with valid input data containing local and remote ranking IDs.
     * - When the worker's [doWork] method is called.
     * - Then the worker should fetch the remote ranking using the provided remote ID.
     * - Then the worker should update the local ranking with data from the remote ranking.
     * - Then the worker should return [ListenableWorker.Result.success] with a message indicating successful synchronization.
     *
     * **Purpose:**
     * This test verifies the successful execution path of the `SyncRankingWorker`. It ensures that when given valid input data,
     * the worker correctly fetches the remote ranking, updates the local ranking, and returns a success result with an appropriate message.
     *
     * **Dependencies:**
     * - [SyncRankingWorker]: The worker class being tested.
     * - [UseCases]: An object containing the use cases used by the worker.
     * - [TestListenableWorkerBuilder]: A helper class for building and testing ListenableWorkers.
     * - [TestWorkerFactory]: A factory for creating workers in tests.
     * - [workDataOf]: A function for creating WorkData objects.
     * - [coEvery]: A function for mocking coroutine functions using MockK.
     * - [assertThat]: A function for making assertions using Truth.
     *
     * **Input:**
     * - `inputData`: WorkData containing the local ranking ID, remote ranking ID, and admin flag.
     *
     * **Expected Output:**
     * - `result`: A [ListenableWorker.Result.success] object containing a message indicating successful synchronization.
     *
     * **Notes:**
     * - The test uses MockK to mock the use cases and verify their interactions.
     * - The test uses Truth to assert the expected output.
     */
    @Test
    fun syncRankingWorker_success_returnsSuccess() = runBlocking {
        // Arrange
        val useCases = mockk<UseCases>()
        val mockkNetworkRanking = NetworkRanking(
            apiId = 1,
            name = "Test Ranking",
            lastUpdated = mockk()
        )

        val inputData =
            workDataOf(
                WorkManagerConstants.WorkData.LOCAL_RANKING_ID to LOCAL_RANKING_ID,
                WorkManagerConstants.WorkData.REMOTE_RANKING_ID to REMOTE_RANKING_ID,
                WorkManagerConstants.WorkData.IS_ADMIN to IS_ADMIN,
            )

        coEvery { useCases.getRemoteRankingUseCase(id = REMOTE_RANKING_ID) } returns mockkNetworkRanking
        coEvery { useCases.updateRankingWithPlayers(any()) } returns Unit

        val worker = TestListenableWorkerBuilder<SyncRankingWorker>(context)
            .setInputData(inputData)
            .setWorkerFactory(TestWorkerFactory(useCases))
            .build()

        val expectedWorkData = workDataOf(
                MESSAGE to context.getString(
                    R.string.ranking_sincronizado_com_sucesso
                )
        )

        // Act
        val result = worker.doWork()

        // Assert
        assertThat(result, `is`(ListenableWorker.Result.success(expectedWorkData)))
    }

    /**
     * Tests the behavior of the `SyncRankingWorker` when a `NetworkErrorException` occurs.
     *
     * This test verifies that when the `getRemoteRankingUseCase` throws a `NetworkErrorException`,
     * the worker returns a `Result.failure` with the error message in the output data.
     *
     * **Scenario:**
     * 1. The worker receives input data with local ranking ID, remote ranking ID, and admin status.
     * 2. The `getRemoteRankingUseCase` is mocked to throw a `NetworkErrorException`.
     * 3. The worker is executed using `doWork()`.
     *
     * **Expected Outcome:**
     * - The worker should return `Result.failure`.
     * - The output data should contain a message indicating a network error, including the
     *   localized message from the exception.
     */
    @Test
    fun syncRankingWorker_networkErrorException_returnsFailure() = runBlocking {
        // Arrange
        val useCases = mockk<UseCases>()
        val inputData = workDataOf(
            WorkManagerConstants.WorkData.LOCAL_RANKING_ID to LOCAL_RANKING_ID,
            WorkManagerConstants.WorkData.REMOTE_RANKING_ID to REMOTE_RANKING_ID,
            WorkManagerConstants.WorkData.IS_ADMIN to IS_ADMIN,
        )
        val networkException = NetworkErrorException("Network error")

        coEvery { useCases.getRemoteRankingUseCase(any(), any()) } throws networkException

        val worker = TestListenableWorkerBuilder<SyncRankingWorker>(context)
            .setInputData(inputData)
            .setWorkerFactory(TestWorkerFactory(useCases))
            .build()

        val expectedWorkData = workDataOf(
            MESSAGE to context.getString(
                R.string.network_error,
                networkException.localizedMessage
            )
        )

        // Act
        val result = worker.doWork()

        // Assert
        assertThat(result, `is`(ListenableWorker.Result.failure(expectedWorkData)))
    }

    /**
     * Tests the behavior of the `SyncRankingWorker` when a `SocketTimeoutException` is thrown
     * during the execution of the `getRemoteRankingUseCase`.
     *
     * **Scenario:**
     * - The worker is initialized with input data containing the local ranking ID, remote ranking ID, and admin status.
     * - The `getRemoteRankingUseCase` is mocked to throw a `SocketTimeoutException`.
     *
     * **Expected Behavior:**
     * - The worker should return a `ListenableWorker.Result.retry()`, indicating that the task should be retried later.
     *
     * **Reasoning:**
     * A `SocketTimeoutException` suggests a temporary network issue. Retrying the task at a later time might be successful
     * when the network connection is restored.
     */
    @Test
    fun syncRankingWorker_socketTimeoutException_returnsRetry() = runBlocking {
        // Arrange
        val useCases = mockk<UseCases>()
        val inputData = workDataOf(
            WorkManagerConstants.WorkData.LOCAL_RANKING_ID to LOCAL_RANKING_ID,
            WorkManagerConstants.WorkData.REMOTE_RANKING_ID to REMOTE_RANKING_ID,
            WorkManagerConstants.WorkData.IS_ADMIN to IS_ADMIN,
        )

        coEvery { useCases.getRemoteRankingUseCase(id = REMOTE_RANKING_ID) } throws SocketTimeoutException()

        val worker = TestListenableWorkerBuilder<SyncRankingWorker>(context)
            .setInputData(inputData)
            .setWorkerFactory(TestWorkerFactory(useCases))
            .build()

        // Act
        val result = worker.doWork()

        // Assert
        assertThat(result, `is`(ListenableWorker.Result.retry()))
    }

    /**
     * Tests the behavior of the SyncRankingWorker when a generic exception occurs during the
     * remote ranking retrieval.
     *
     * **Scenario:**
     * - A generic RuntimeException is thrown when calling `getRemoteRankingUseCase`.
     *
     * **Expected Outcome:**
     * - The worker should return a Result.failure status.
     * - The output WorkData should contain an error message indicating an unexpected error
     *   along with the exception's localized message.
     */
    @Test
    fun syncRankingWorker_genericException_returnsFailure() = runBlocking {
        // Arrange
        val useCases = mockk<UseCases>()
        val inputData = workDataOf(
            WorkManagerConstants.WorkData.LOCAL_RANKING_ID to LOCAL_RANKING_ID,
            WorkManagerConstants.WorkData.REMOTE_RANKING_ID to REMOTE_RANKING_ID,
            WorkManagerConstants.WorkData.IS_ADMIN to IS_ADMIN,
        )
        val genericException = RuntimeException("Unexpected error")
        coEvery { useCases.getRemoteRankingUseCase(id = REMOTE_RANKING_ID) } throws genericException

        val worker = TestListenableWorkerBuilder<SyncRankingWorker>(context)
            .setInputData(inputData)
            .setWorkerFactory(TestWorkerFactory(useCases))
            .build()

        val expectedWorkData = workDataOf(
            MESSAGE to context.getString(
                R.string.um_erro_inesperado_aconteceu,
                genericException.localizedMessage
            )
        )

        // Act
        val result = worker.doWork()

        // Assert
        assertThat(result, `is`(ListenableWorker.Result.failure(expectedWorkData)))
    }
}