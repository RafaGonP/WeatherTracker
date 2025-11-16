package com.rafagonp.weathertracker.viewmodels

import app.cash.turbine.test
import com.rafagonp.weathertracker.domain.model.DaysListBO
import com.rafagonp.weathertracker.domain.model.Resource
import com.rafagonp.weathertracker.domain.model.Status
import com.rafagonp.weathertracker.domain.usecase.GetPredictionPerDaysUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

@ExperimentalCoroutinesApi
class DaysListViewModelTest {
    @MockK
    private lateinit var mockGetPredictionPerDaysUseCase: GetPredictionPerDaysUseCase
    private lateinit var viewModel: DaysListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {

        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN daysListFlow emit loading then success WHEN UseCase returns data THEN return success data`() = runTest(testDispatcher) {

        every { mockGetPredictionPerDaysUseCase.invoke() } returns successFlow

        viewModel = DaysListViewModel(mockGetPredictionPerDaysUseCase)

        viewModel.daysListFlow.test(timeout = 100.milliseconds) {

            val initial = awaitItem()
            assertEquals(Status.LOADING, initial.status)

            val success = awaitItem()
            assertEquals(Status.SUCCESS, success.status)
            assertEquals(mockDaysListBO, success.data)

            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) { mockGetPredictionPerDaysUseCase.invoke() }
    }

    @Test
    fun `GIVEN daysListFlow should emit loading then error WHEN UseCase returns error THEN emit loading then error`() = runTest(testDispatcher) {

        every { mockGetPredictionPerDaysUseCase.invoke() } returns errorFlow

        viewModel = DaysListViewModel(mockGetPredictionPerDaysUseCase)

        viewModel.daysListFlow.test(timeout = 100.milliseconds) {

            val initial = awaitItem()
            assertEquals(Status.LOADING, initial.status)


            val error = awaitItem()
            assertEquals(Status.ERROR, error.status)
            assertEquals(errorMessage, error.message)

            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) { mockGetPredictionPerDaysUseCase.invoke() }
    }

    companion object{
        val mockDaysListBO = DaysListBO(
            days = listOf()
        )
        val successFlow = flowOf(Resource.success(mockDaysListBO))

        val errorMessage = "Error de red"

        val errorFlow = flowOf(Resource.error(errorMessage, null))
    }
}