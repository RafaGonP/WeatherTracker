package com.rafagonp.weathertracker.domain.usecase

import app.cash.turbine.test
import com.rafagonp.weathertracker.domain.model.DaysListBO
import com.rafagonp.weathertracker.domain.model.Resource
import com.rafagonp.weathertracker.domain.model.Status
import com.rafagonp.weathertracker.domain.repository.WeatherRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WeatherUseCasesTest {

    @MockK
    private lateinit var mockWeatherRepository: WeatherRepository

    private lateinit var useCase: GetPredictionPerDaysUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = GetPredictionPerDaysUseCase(mockWeatherRepository)
    }

    @Test
    fun `GIVEN valid parameters WHEN get prediction per days use case THEN call prediction per days repository`() =
        runTest {
            coEvery {
                mockWeatherRepository.getPredictionPerDays(any(), any())
            } returns flowOf(Resource.success(DaysListBO(listOf())))

            useCase.invoke()

            coVerify(exactly = 1) {
                mockWeatherRepository.getPredictionPerDays(any(), any())
            }
        }

    @Test
    fun `GIVEN valid parameters WHEN get prediction per days use case THEN get successful result`() =
        runTest {
            val mockDaysListBO = DaysListBO(listOf())
            coEvery {
                mockWeatherRepository.getPredictionPerDays(any(), any())
            } returns flowOf(Resource.success(DaysListBO(listOf())))

            val resultFlow = useCase.invoke()
            resultFlow.test {
                val emittedResource = awaitItem()
                assertEquals(Status.SUCCESS, emittedResource.status)
                assertEquals(mockDaysListBO, emittedResource.data)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `GIVEN valid parameters WHEN get prediction per days use case THEN return error`() =
        runTest {
            val errorMessage = "Network Error"
            val errorFlow = flowOf(Resource.error(errorMessage, null))

            every {
                mockWeatherRepository.getPredictionPerDays(any(), any())
            } returns errorFlow

            val resultFlow = useCase.invoke()

            resultFlow.test {
                val emittedResource = awaitItem()
                assertEquals(Status.ERROR, emittedResource.status)
                assertEquals(errorMessage, emittedResource.message)

                cancelAndIgnoreRemainingEvents()
            }
        }

    companion object {
        const val EXCLUDE = ""
        const val UNITS = ""
    }
}