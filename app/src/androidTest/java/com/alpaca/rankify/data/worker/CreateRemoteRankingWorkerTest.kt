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
import com.alpaca.rankify.domain.model.Ranking
import com.alpaca.rankify.domain.use_cases.UseCases
import com.alpaca.rankify.util.constant.WorkManagerConstants
import com.alpaca.rankify.util.constant.WorkManagerConstants.WorkData.MESSAGE
import io.mockk.Ordering
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.net.SocketTimeoutException

/**
 * Instrumented test for [CreateRemoteRankingWorker], which will execute on an Android device.
 *
 * This class contains unit tests for the [CreateRemoteRankingWorker] class, ensuring it correctly
 * handles the creation of remote rankings, including success, error, and network scenarios.
 *
 * The tests utilize MockK for mocking dependencies and WorkManager's testing infrastructure for
 * simulating worker execution.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class CreateRemoteRankingWorkerTest {
    private lateinit var context: Context

    companion object {
        private const val LOCAL_RANKING_ID = 1L
        private const val RANKING_NAME = "Test Ranking"
        private const val ADMIN_PASSWORD = "1234"
    }

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    /**
     * Tests the [CreateRemoteRankingWorker] when the remote ranking is created successfully.
     *
     * This test verifies the following:
     * 1. **Retrieves Local Ranking:** The worker fetches the local ranking using the provided local ranking ID.
     * 2. **Creates Remote Ranking:** The worker calls the `createRemoteRanking` use case to create the corresponding remote ranking.
     * 3. **Deletes Existing Players:** The worker deletes all existing players associated with the local ranking to ensure synchronization with the remote ranking.
     * 4. **Updates Local Ranking:** The worker updates the local ranking with the player data from the newly created remote ranking.
     * 5. **Returns Success:** The worker returns a [ListenableWorker.Result.success] status along with a message confirming the successful creation of the remote ranking.
     *
     * **Scenario:** A request is made to create a remote ranking based on an existing local ranking. The operation is successful, resulting in the creation of the remote ranking and synchronization with the local ranking.
     *
     * **Assertions:**
     * - The worker should return a `Result.success` status.
     * - The result should contain a message indicating the successful creation of the remote ranking (using the string resource `R.string.ranking_criado_no_servidor_com_sucesso`).
     */
    @Test
    fun createRemoteRankingWorker_success_returnsSuccessWithMessage() = runBlocking {
        // Arrange
        val useCases = mockk<UseCases>()
        val mockRanking = Ranking(
            localId = LOCAL_RANKING_ID,
            name = RANKING_NAME,
            adminPassword = ADMIN_PASSWORD
        )
        val mockRemoteRanking = NetworkRanking(
            apiId = LOCAL_RANKING_ID,
            name = RANKING_NAME,
            isAdmin = true,
            players = listOf(),
            lastUpdated = mockk()
        )

        coEvery { useCases.getRanking(LOCAL_RANKING_ID) } returns flowOf(mockRanking)
        coEvery { useCases.createRemoteRanking(any()) } returns mockRemoteRanking
        coEvery { useCases.deleteAllPlayers(LOCAL_RANKING_ID) } returns Unit
        coEvery { useCases.updateRankingWithPlayers(any()) } returns Unit

        val worker = TestListenableWorkerBuilder<CreateRemoteRankingWorker>(context)
            .setInputData(workDataOf(WorkManagerConstants.WorkData.LOCAL_RANKING_ID to LOCAL_RANKING_ID))
            .setWorkerFactory(TestWorkerFactory(useCases))
            .build()

        val expectedWorkData =
            workDataOf(
                MESSAGE to context.getString(R.string.ranking_criado_no_servidor_com_sucesso)
            )

        // Act
        val result = worker.doWork()

        // Assert
        assertThat(result, `is`(ListenableWorker.Result.success(expectedWorkData)))
    }


    /**
     * Tests the behavior of [CreateRemoteRankingWorker] when the ranking is not found.
     *
     * This test verifies that when the [UseCases.getRanking] function returns a flow emitting `null`,
     * indicating that the ranking does not exist, the worker should:
     *
     * 1. Return a [ListenableWorker.Result.failure] status.
     * 2. Include an error message in the output [androidx.work.Data] using the keys [MESSAGE].
     *    The message should indicate that an unexpected error occurred, specifically mentioning that
     *    the ranking does not exist.
     *
     * **Scenario:**
     * - The `getRanking` use case is mocked to return a flow emitting `null`.
     * - A [CreateRemoteRankingWorker] is created using a test worker builder.
     * - The worker's `doWork()` function is executed.
     *
     * **Assertions:**
     * - The result of `doWork()` is a [ListenableWorker.Result.failure].
     * - The output [androidx.work.Data] contains the expected error message.
     */
    @Test
    fun createRemoteRankingWorker_noRankingFound_returnsErrorMessage() = runBlocking {
        // Arrange
        val useCases = mockk<UseCases>()
        coEvery { useCases.getRanking(any()) } returns flowOf(null)

        val worker = TestListenableWorkerBuilder<CreateRemoteRankingWorker>(context)
            .setWorkerFactory(TestWorkerFactory(useCases))
            .build()

        val expectedWorkData =
            workDataOf(
                MESSAGE to context.getString(
                            R.string.um_erro_inesperado_aconteceu,
                            context.getString(R.string.oopss_parace_que_o_ranking_nao_existe_mais)
                        )
            )

        // Act
        val result = worker.doWork()

        // Assert
        assertThat(
            result,
            `is`(ListenableWorker.Result.failure(expectedWorkData))
        )
    }

    /**
     * Tests the behavior of [CreateRemoteRankingWorker] when attempting to create a remote ranking
     * for a local ranking that does not have an admin password set.
     *
     * **Scenario:** The worker is initialized with the ID of a local ranking. The local ranking
     * is retrieved and found to be missing an admin password.
     *
     * **Expected Behavior:**
     * - The worker should return a [ListenableWorker.Result.failure] status.
     * - The failure should include an error message indicating that the ranking was created
     *   without an admin password.
     *
     * This test verifies that the worker correctly handles the case where the local ranking
     * lacks the necessary admin password, preventing the creation of the remote ranking and
     * providing an informative error message.
     */
    @Test
    fun createRemoteRankingWorker_noAdminPassword_returnsErrorMessage() = runBlocking {
        // Arrange
        val useCases = mockk<UseCases>()
        val mockRanking = Ranking(
            localId = LOCAL_RANKING_ID,
            name = RANKING_NAME,
            //No admin password
        )

        coEvery { useCases.getRanking(id = LOCAL_RANKING_ID) } returns flowOf(mockRanking)

        val worker = TestListenableWorkerBuilder<CreateRemoteRankingWorker>(context)
            .setInputData(workDataOf(WorkManagerConstants.WorkData.LOCAL_RANKING_ID to LOCAL_RANKING_ID))
            .setWorkerFactory(TestWorkerFactory(useCases))
            .build()

        val expectedWorkData =
            workDataOf(
                MESSAGE to context.getString(
                            R.string.um_erro_inesperado_aconteceu,
                            context.getString(R.string.ranking_criado_sem_senha_de_administrador)
                        )
            )

        // Act
        val result = worker.doWork()

        // Assert
        assertThat(result, `is`(ListenableWorker.Result.failure(expectedWorkData)))
    }

    /**
     * Tests the behavior of [CreateRemoteRankingWorker] when a network error occurs during the
     * creation of a remote ranking.
     *
     * **Scenario:**
     * - The worker is initialized with a local ranking ID.
     * - The `getRanking` use case is mocked to return a valid ranking.
     * - The `createRemoteRanking` use case is mocked to throw a [NetworkErrorException].
     *
     * **Expected Behavior:**
     * - The worker should return a [ListenableWorker.Result.failure] with a work data containing an
     *   error message indicating the network error.
     *
     * **Verification:**
     * - The result of the worker's `doWork()` method is asserted to be a failure.
     * - The failure's work data is asserted to contain the expected error message, which is formatted
     *   using the `network_error` string resource and the exception's localized message.
     */
    @Test
    fun createRemoteRankingWorker_networkError_returnsFailureWithMessage() = runBlocking {
        // Arrange
        val useCases = mockk<UseCases>()
        val mockRanking = Ranking(
            localId = LOCAL_RANKING_ID,
            name = RANKING_NAME,
            adminPassword = ADMIN_PASSWORD
        )
        val networkException = NetworkErrorException("Network error")

        coEvery { useCases.getRanking(id = LOCAL_RANKING_ID) } returns flowOf(mockRanking)
        coEvery { useCases.createRemoteRanking(any()) } throws networkException

        val worker = TestListenableWorkerBuilder<CreateRemoteRankingWorker>(context)
            .setInputData(workDataOf(WorkManagerConstants.WorkData.LOCAL_RANKING_ID to LOCAL_RANKING_ID))
            .setWorkerFactory(TestWorkerFactory(useCases))
            .build()

        val expectedWorkData =
            workDataOf(
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
     * Tests the behavior of `CreateRemoteRankingWorker` when a `SocketTimeoutException` occurs
     * during the attempt to create a remote ranking.
     *
     * **Scenario:**
     * - The worker is initialized with a local ranking ID.
     * - The `getRanking` use case is mocked to return a valid local ranking.
     * - The `createRemoteRanking` use case is mocked to throw a `SocketTimeoutException`,
     *   simulating a network timeout.
     *
     * **Expected Outcome:**
     * - The worker should return `ListenableWorker.Result.retry()`, indicating that the
     *   operation should be retried later.
     *
     * **Rationale:**
     * A `SocketTimeoutException` suggests a temporary network issue. Retrying the operation
     * later might be successful once the network connection is restored.
     */
    @Test
    fun createRemoteRankingWorker_timeoutError_returnsRetry() = runBlocking {
        // Arrange
        val useCases = mockk<UseCases>()
        val mockRanking = Ranking(
            localId = LOCAL_RANKING_ID,
            name = RANKING_NAME,
            adminPassword = ADMIN_PASSWORD
        )

        coEvery { useCases.getRanking(id = LOCAL_RANKING_ID) } returns flowOf(mockRanking)
        coEvery { useCases.createRemoteRanking(any()) } throws SocketTimeoutException()

        val worker = TestListenableWorkerBuilder<CreateRemoteRankingWorker>(context)
            .setInputData(workDataOf(WorkManagerConstants.WorkData.LOCAL_RANKING_ID to LOCAL_RANKING_ID))
            .setWorkerFactory(TestWorkerFactory(useCases))
            .build()

        // Act
        val result = worker.doWork()

        // Assert
        assertThat(result, `is`(ListenableWorker.Result.retry()))
    }

    /**
     * Tests the [CreateRemoteRankingWorker] when the remote ranking creation is successful.
     *
     * This test verifies that the worker correctly interacts with the provided `UseCases`
     * to create a remote ranking based on a local ranking.
     *
     * **Scenario:** A request is made to create a remote ranking for a given local ranking ID.
     * The operation is successful.
     *
     * **Verification:**
     * 1. **Retrieves local ranking:** The worker first fetches the local ranking data using
     *    `UseCases.getRanking()` with the provided ranking ID.
     * 2. **Creates remote ranking:** It then uses this local ranking data to create a
     *    remote ranking on the server by calling `UseCases.createRemoteRanking()`.
     * 3. **Deletes existing local players:**  To ensure data consistency, the worker deletes
     *    all existing players associated with the local ranking using `UseCases.deleteAllPlayers()`.
     * 4. **Updates local ranking with remote data:** The worker updates the local ranking
     *    with the players from the newly created remote ranking by calling
     *    `UseCases.updateRankingWithPlayers()`.
     * 5. **Returns success:**  The worker returns a successful `Result` with a success
     *    message in the output data.
     *
     * **Methodology:**
     * - **Mocking:** Uses MockK to mock the `UseCases` and control their behavior.
     * - **Verification:** Uses MockK's `coVerify` to ensure the use cases are called in
     *   the expected sequence and with the correct parameters.
     * - **Assertion:** Uses Hamcrest's `assertThat` to verify the worker's result.
     */
    @Test
    fun createRemoteRankingWorker_success_callsAllUseCases() = runBlocking {
        // Arrange
        val useCases = mockk<UseCases>()
        val mockRanking = Ranking(
            localId = LOCAL_RANKING_ID,
            name = RANKING_NAME,
            adminPassword = ADMIN_PASSWORD
        )
        val mockRemoteRanking = NetworkRanking(
            apiId = LOCAL_RANKING_ID,
            name = RANKING_NAME,
            isAdmin = true,
            players = listOf(),
            lastUpdated = mockk()
        )

        coEvery { useCases.getRanking(LOCAL_RANKING_ID) } returns flowOf(mockRanking)
        coEvery { useCases.createRemoteRanking(any()) } returns mockRemoteRanking
        coEvery { useCases.deleteAllPlayers(LOCAL_RANKING_ID) } returns Unit
        coEvery { useCases.updateRankingWithPlayers(any()) } returns Unit

        val worker = TestListenableWorkerBuilder<CreateRemoteRankingWorker>(context)
            .setInputData(workDataOf(WorkManagerConstants.WorkData.LOCAL_RANKING_ID to LOCAL_RANKING_ID))
            .setWorkerFactory(TestWorkerFactory(useCases))
            .build()


        val expectedWorkData =
            workDataOf(
                MESSAGE to context.getString(R.string.ranking_criado_no_servidor_com_sucesso)
            )

        // Act
        val result = worker.doWork()

        // Assert
        assertThat(result, `is`(ListenableWorker.Result.success(expectedWorkData)))

        coVerify(ordering = Ordering.SEQUENCE) {
            useCases.getRanking(LOCAL_RANKING_ID)
            useCases.createRemoteRanking(any())
            useCases.deleteAllPlayers(LOCAL_RANKING_ID)
            useCases.updateRankingWithPlayers(any())
        }
    }
}