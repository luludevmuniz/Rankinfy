package com.alpaca.rankify.data.repository

import android.accounts.NetworkErrorException
import androidx.work.WorkManager
import com.alpaca.rankify.data.local.entities.RankingEntity
import com.alpaca.rankify.data.local.entities.RankingWithPlayers
import com.alpaca.rankify.data.remote.models.NetworkRanking
import com.alpaca.rankify.domain.model.CreateRankingDTO
import com.alpaca.rankify.domain.model.Ranking
import com.alpaca.rankify.domain.model.mappers.asEntity
import com.alpaca.rankify.domain.model.mappers.asExternalModel
import com.alpaca.rankify.domain.repository.LocalDataSource
import com.alpaca.rankify.domain.repository.RemoteDataSource
import com.alpaca.rankify.util.RequestState
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RankingRepositoryTest {
    private lateinit var repository: RankingRepository
    private val localDataSource: LocalDataSource = mockk()
    private val remoteDataSource: RemoteDataSource = mockk()
    private val workManager: WorkManager = mockk()

    @Before
    fun setUp() {
        repository = RankingRepository(
            local = localDataSource,
            remote = remoteDataSource,
            workManager = workManager
        )
    }

    @Test
    fun `createRanking should return Success with ID when saveRanking is successful`() =
        runBlocking {
            // Arrange
            val rankingName = "Test Ranking"
            val rankingAdminPassword = "admin123"
            val rankingEntity = RankingEntity(
                name = rankingName,
                isAdmin = true,
                adminPassword = rankingAdminPassword
            )
            val rankingId = 1L
            coEvery { localDataSource.saveRanking(rankingEntity) } returns rankingId

            // Act
            val result = repository.createRanking(
                rankingName = rankingName,
                rankingAdminPassword = rankingAdminPassword
            )

            // Assert
            assertEquals(RequestState.Success(rankingId), result)
            coVerify { localDataSource.saveRanking(rankingEntity) }
        }

    @Test
    fun `updateRanking should call localDataSource updateRanking`() = runBlocking {
        // Arrange
        val rankingEntity = RankingEntity(
            name = "Test Ranking",
            isAdmin = true,
            adminPassword = "admin123"
        )

        coEvery { localDataSource.updateRanking(rankingEntity) } just Runs

        // Act
        repository.updateRanking(rankingEntity)

        // Assert
        coVerify { localDataSource.updateRanking(rankingEntity) }
    }

    @Test
    fun `updateRankingWithPlayers should call localDataSource updateRankingWithPlayers`() =
        runBlocking {
            // Arrange
            val ranking = Ranking(
                localId = 1L,
                remoteId = 1L,
                name = "Test Ranking",
                isAdmin = true,
            )
            val rankingWithPlayers = RankingWithPlayers(
                ranking = ranking.asEntity(),
                players = emptyList()
            )

            coEvery { localDataSource.updateRankingWithPlayers(rankingWithPlayers) } just Runs

            // Act
            repository.updateRankingWithPlayers(ranking)

            // Assert
            coVerify { localDataSource.updateRankingWithPlayers(rankingWithPlayers) }
        }

    @Test
    fun `getRanking should return Flow of Ranking`() = runBlocking {
        // Arrange
        val rankingId = 1L
        val rankingEntity = RankingEntity(
            localId = rankingId,
            name = "Test Ranking",
            isAdmin = true,
            adminPassword = "admin123"
        )
        val rankingWithPlayers = RankingWithPlayers(
            ranking = rankingEntity,
            players = emptyList()
        )

        coEvery { localDataSource.getRanking(rankingId) } returns flowOf(rankingWithPlayers)

        // Act
        val result = repository.getRanking(rankingId).firstOrNull()

        // Assert
        assertEquals(rankingEntity.asExternalModel(), result)
    }

    @Test
    fun `getAllRankings should return Flow of list of Ranking`() = runBlocking {
        // Arrange
        val rankings = listOf(
            RankingEntity(
                localId = 1L,
                name = "Ranking 1",
                isAdmin = true,
                adminPassword = "pass1"
            ),
            RankingEntity(
                localId = 2L,
                name = "Ranking 2",
                isAdmin = false,
                adminPassword = "pass2"
            )
        )

        coEvery { localDataSource.getAllRankings() } returns flowOf(rankings)

        // Act
        val result = repository.getAllRankings().firstOrNull()

        // Assert
        assertEquals(rankings.map { it.asExternalModel() }, result)
    }

    @Test
    fun `createRemoteRanking should call remoteDataSource createRanking`() = runBlocking {
        // Arrange
        val rankingDTO = CreateRankingDTO(
            name = "Test Ranking",
            adminPassword = "admin123",
            mobileId = 1L
        )
        val networkRanking =
            NetworkRanking(apiId = 1L, name = "Test Ranking", lastUpdated = mockk())
        coEvery { remoteDataSource.createRanking(rankingDTO) } returns networkRanking

        // Act
        val result = repository.createRemoteRanking(rankingDTO)

        // Assert
        assertEquals(networkRanking, result)
        coVerify { remoteDataSource.createRanking(rankingDTO) }
    }

    @Test
    fun `getRemoteRanking should call remoteDataSource getRanking`() = runBlocking {
        // Arrange
        val rankingId = 1L
        val password = "admin123"
        val networkRanking =
            NetworkRanking(apiId = rankingId, name = "Test Ranking", lastUpdated = mockk())
        coEvery { remoteDataSource.getRanking(rankingId, password) } returns networkRanking

        // Act
        val result = repository.getRemoteRanking(rankingId, password)

        // Assert
        assertEquals(networkRanking, result)
        coVerify { remoteDataSource.getRanking(rankingId, password) }
    }

    @Test
    fun `deleteRanking should call localDataSource deleteRanking`() = runBlocking {
        // Arrange
        val rankingId = 1L
        coEvery { localDataSource.deleteRanking(rankingId) } returns 1

        // Act
        val result = repository.deleteRanking(rankingId)

        // Assert
        assertEquals(1, result)
        coVerify { localDataSource.deleteRanking(rankingId) }
    }

    @Test
    fun `deleteRemoteRanking should call remoteDataSource deleteRanking`() = runBlocking {
        // Arrange
        val rankingId = 1L
        coEvery { remoteDataSource.deleteRanking(rankingId) } just Runs

        // Act
        repository.deleteRemoteRanking(rankingId)

        // Assert
        coVerify { remoteDataSource.deleteRanking(rankingId) }
    }

    @Test
    fun `searchRanking should return Success when fetchAndSaveRanking is successful`() = runBlocking {
        // Arrange
        val rankingId = 1L
        val adminPassword = "admin123"
        val networkRanking = NetworkRanking(apiId = rankingId, name = "Test Ranking", lastUpdated = mockk())
        val rankingWithPlayers = RankingWithPlayers(
            ranking = networkRanking.asExternalModel(mobileId = 0).asEntity(),
            players = emptyList()
        )
        coEvery { remoteDataSource.getRanking(rankingId, adminPassword) } returns networkRanking
        coEvery { localDataSource.getRankingWithRemoteId(networkRanking.apiId) } returns flowOf(null)
        coEvery { localDataSource.saveRankingWithPlayers(rankingWithPlayers) } returns 1L

        // Act
        val result = repository.searchRanking(rankingId, adminPassword)

        // Assert
        assertEquals(RequestState.Success(1L), result)
        coVerify { remoteDataSource.getRanking(rankingId, adminPassword) }
        coVerify { localDataSource.saveRankingWithPlayers(rankingWithPlayers) }
    }


    @Test
    fun `searchRanking should return Error when NetworkErrorException occurs`() = runBlocking {
        // Arrange
        val rankingId = 1L
        val adminPassword = "admin123"
        val networkException = NetworkErrorException("Network error")
        coEvery {
            remoteDataSource.getRanking(
                id = rankingId,
                password = adminPassword
            )
        } throws networkException

        // Act
        val result = repository.searchRanking(
            id = rankingId,
            adminPassword = adminPassword
        )

        // Assert
        assertEquals(
            RequestState.Error("Network error: ${networkException.localizedMessage}"),
            result
        )
        coVerify {
            remoteDataSource.getRanking(
                id = rankingId,
                password = adminPassword
            )
        }
    }
}