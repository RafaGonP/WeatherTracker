package com.rafagonp.weathertracker.data.remote.datasource.impl

import com.rafagonp.weathertracker.data.local.OpenWeatherAPIKey
import com.rafagonp.weathertracker.data.remote.model.GetPredictionPerDaysResponseDTO
import com.rafagonp.weathertracker.data.remote.service.WeatherService
import com.rafagonp.weathertracker.data.session.datasource.LAT
import com.rafagonp.weathertracker.data.session.datasource.LONG
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class WeatherRemoteDataSourceImplTest {

    @MockK
    private lateinit var mockWeatherService: WeatherService

    @MockK
    private lateinit var mockOpenWeatherAPIKey: OpenWeatherAPIKey

    private lateinit var dataSource: WeatherRemoteDataSourceImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        coEvery { mockOpenWeatherAPIKey.apiKey } returns MOCK_API_KEY

        dataSource = WeatherRemoteDataSourceImpl(
            weatherService = mockWeatherService,
            openWeatherAPIKey = mockOpenWeatherAPIKey
        )
    }

    @Test
    fun `GIVEN valid exclude and units WHEN get prediction per days THEN call prediction per days service `() = runTest {
        successGiven()

        dataSource.getPredictionPerDays(EXCLUDE, UNITS)

        coVerify(exactly = 1) {
            mockWeatherService.getPredictionPerDays(
                LAT,
                LONG,
                EXCLUDE,
                MOCK_API_KEY,
                UNITS
            )
        }
    }

    @Test
    fun `GIVEN valid exclude and units WHEN get prediction per days THEN return success`() = runTest {
        successGiven()

        val result = dataSource.getPredictionPerDays(EXCLUDE, UNITS)

        assert(result.isRight())
    }

    @Test
    fun `GIVEN valid exclude and units WHEN get prediction per days THEN return error`() = runTest {
        errorGiven()

        val result = dataSource.getPredictionPerDays(EXCLUDE, UNITS)

        assert(result.isLeft())
    }

    private fun successGiven(){
        coEvery {
            mockWeatherService.getPredictionPerDays(
                LAT,
                LONG,
                EXCLUDE,
                MOCK_API_KEY,
                UNITS
            )
        } returns mockResponse
    }

    private fun errorGiven(){
        val mockException = RuntimeException("Network Error")

        coEvery {
            mockWeatherService.getPredictionPerDays(
                any(), any(), any(), any(), any()
            )
        } throws mockException
    }

    companion object {
        const val MOCK_API_KEY = "idfsg7348oydfg384"
        const val EXCLUDE = ""
        const val UNITS = ""

        val mockResponse: Response<GetPredictionPerDaysResponseDTO> = Response.success(
            GetPredictionPerDaysResponseDTO())
    }
}