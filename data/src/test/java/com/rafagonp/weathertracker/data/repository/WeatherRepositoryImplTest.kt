package com.rafagonp.weathertracker.data.repository

import app.cash.turbine.test
import arrow.core.Either
import com.rafagonp.weathertracker.data.local.DaysListDao
import com.rafagonp.weathertracker.data.remote.datasource.WeatherRemoteDataSource
import com.rafagonp.weathertracker.data.remote.dispatcher.DispatcherProvider
import com.rafagonp.weathertracker.data.remote.model.DaysListEntity
import com.rafagonp.weathertracker.data.remote.model.toModel
import com.rafagonp.weathertracker.domain.model.CustomError
import com.rafagonp.weathertracker.domain.model.Status
import com.rafagonp.weathertracker.utils.TimeHelper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcherProvider(testDispatcher: TestDispatcher) : DispatcherProvider {
    override val main = testDispatcher
    override val io = testDispatcher
    override val default = testDispatcher
}

@ExperimentalCoroutinesApi
class WeatherRepositoryImplTest {
    @MockK
    private lateinit var mockWeatherRemoteDataSource: WeatherRemoteDataSource
    @MockK
    private lateinit var mockDaysListDao: DaysListDao

    private val testDispatcher = StandardTestDispatcher()
    private val testDispatcherProvider = TestDispatcherProvider(testDispatcher)

    private lateinit var repository: WeatherRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mockkObject(TimeHelper)
        coEvery { TimeHelper.getNow() } returns 1_000_000L

        repository = WeatherRepositoryImpl(
            mockWeatherRemoteDataSource,
            mockDaysListDao,
            testDispatcherProvider
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    private fun createCachedEntity(time: Long): DaysListEntity {
        val entity = mockk<DaysListEntity>(relaxed = true) {
            every { lastUpdated } returns time
        }
        return entity
    }

    @Test
    fun `GIVEN cache is empty WHEN getPredictionPerDays THEN should fetch remote data`() = runTest(testDispatcher) {
        val newRemoteEntity = createCachedEntity(timeValue)

        every { mockDaysListDao.getDaysList() } returns flowOf(null)
        coEvery {
            mockWeatherRemoteDataSource.getPredictionPerDays(any(), any())
        } returns Either.Right(newRemoteEntity)
        coEvery { mockDaysListDao.insertDaysList(newRemoteEntity) } returns Unit

        repository.getPredictionPerDays("exclude", "units").test {

            advanceTimeBy(100)

            coVerify(exactly = 1) {
                mockWeatherRemoteDataSource.getPredictionPerDays(any(), any())
            }

            coVerify(exactly = 1) {
                mockDaysListDao.insertDaysList(any())
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GIVEN cache is valid WHEN getPredictionPerDays THEN emit cached data and NOT call remote`() = runTest(testDispatcher) {
        val validCachedEntity = createCachedEntity(oneMinuteAgo)
        val expectedBo = validCachedEntity.toModel()

        every { mockDaysListDao.getDaysList() } returns flowOf(validCachedEntity)

        coVerify(exactly = 0) { mockWeatherRemoteDataSource.getPredictionPerDays(any(), any()) }

        repository.getPredictionPerDays("exclude", "units").test {

            val emittedResource = awaitItem()
            assertEquals(Status.SUCCESS, emittedResource.status)
            assertEquals(expectedBo, emittedResource.data)

            advanceTimeBy(100)

            coVerify(exactly = 0) { mockWeatherRemoteDataSource.getPredictionPerDays(any(), any()) }
            coVerify(exactly = 0) { mockDaysListDao.insertDaysList(any()) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GIVEN cache expired WHEN getPredictionPerDays THEN emit cached data then fetch remote`() = runTest(testDispatcher) {
        val expiredCachedEntity = createCachedEntity(fifteenMinutesAgo)
        val expiredBo = expiredCachedEntity.toModel()
        val newRemoteEntity = createCachedEntity(timeValue)

        every { mockDaysListDao.getDaysList() } returns flowOf(expiredCachedEntity)
        coEvery {
            mockWeatherRemoteDataSource.getPredictionPerDays(any(), any())
        } returns Either.Right(newRemoteEntity)
        coEvery { mockDaysListDao.insertDaysList(newRemoteEntity) } returns Unit


        repository.getPredictionPerDays("exclude", "units").test {

            val firstEmission = awaitItem()
            assertEquals(Status.SUCCESS, firstEmission.status)
            assertEquals(expiredBo, firstEmission.data)

            advanceUntilIdle()

            coVerify(exactly = 1) {
                mockWeatherRemoteDataSource.getPredictionPerDays("exclude", "units")
            }

            coVerify(exactly = 1) {
                mockDaysListDao.insertDaysList(any())
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GIVEN network error and cache expired WHEN getPredictionPerDays THEN should emit error if cache is expired and remote fetch fails`() = runTest(testDispatcher) {
        val expiredCachedEntity = createCachedEntity(fifteenMinutesAgo)
        val expiredBo = expiredCachedEntity.toModel()

        every { mockDaysListDao.getDaysList() } returns flowOf(expiredCachedEntity)

        val errorMessage = "Remote Server Error"
        coEvery {
            mockWeatherRemoteDataSource.getPredictionPerDays(any(), any())
        } returns Either.Left(CustomError.Unknown(errorMessage))

        repository.getPredictionPerDays("exclude", "units").test {

            val firstEmission = awaitItem()
            assertEquals(Status.SUCCESS, firstEmission.status)
            assertEquals(expiredBo, firstEmission.data)

            advanceUntilIdle()

            val errorEmission = awaitItem()
            assertEquals(Status.ERROR, errorEmission.status)
            assertTrue(errorEmission.message!!.contains(errorMessage))

            coVerify(exactly = 1) {
                mockWeatherRemoteDataSource.getPredictionPerDays("exclude", "units")
            }

            coVerify(exactly = 0) { mockDaysListDao.insertDaysList(any()) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    companion object{
        val timeValue = 1_000_000L
        val oneMinuteAgo = timeValue - TimeUnit.MINUTES.toMillis(1)
        val fifteenMinutesAgo = timeValue - TimeUnit.MINUTES.toMillis(15)
    }
}