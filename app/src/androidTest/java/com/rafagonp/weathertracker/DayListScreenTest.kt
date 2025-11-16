package com.rafagonp.weathertracker

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.rafagonp.weathertracker.domain.model.DayBO
import com.rafagonp.weathertracker.domain.model.DaysListBO
import com.rafagonp.weathertracker.domain.model.Resource
import com.rafagonp.weathertracker.domain.model.TempBO
import com.rafagonp.weathertracker.domain.model.WeatherBO
import com.rafagonp.weathertracker.domain.model.myCapitalize
import com.rafagonp.weathertracker.ui.ERROR_TEXT_TAG
import com.rafagonp.weathertracker.ui.LOADING_TEXT_TAG
import com.rafagonp.weathertracker.ui.SUCCESS_LIST_TAG
import com.rafagonp.weathertracker.ui.screen.DaysListContent
import com.rafagonp.weathertracker.ui.theme.WeatherTrackerTheme
import org.junit.Rule
import org.junit.Test

class DaysListContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun daysListContent_whenLoading_showsLoadingText() {
        val loadingResource = Resource.loading(DaysListBO(mutableListOf()))

        composeTestRule.setContent {
            WeatherTrackerTheme {
                DaysListContent(
                    loadingResource,
                    onItemClick = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(LOADING_TEXT_TAG)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag(SUCCESS_LIST_TAG, useUnmergedTree = true).assertDoesNotExist()
    }

    @Test
    fun daysListContent_whenError_showsErrorMessage() {
        val errorMessage = "Error message"
        val errorResource = Resource.error(errorMessage, null)

        composeTestRule.setContent {
            DaysListContent(
                errorResource,
                onItemClick = {}
            )
        }

        composeTestRule.onNodeWithTag(ERROR_TEXT_TAG)
            .assertIsDisplayed()
            .assertTextEquals(errorMessage)

        composeTestRule.onNodeWithTag(LOADING_TEXT_TAG, useUnmergedTree = true).assertDoesNotExist()
        composeTestRule.onNodeWithTag(SUCCESS_LIST_TAG, useUnmergedTree = true).assertDoesNotExist()
    }

    @Test
    fun daysListContent_whenSuccess_showsListAndItemsAreClickable() {
        val successResource = Resource.success(mockDaysList)
        var clickedDay: DayBO? = null

        composeTestRule.setContent {
            DaysListContent(
                successResource,
                onItemClick = { day -> clickedDay = day }
            )
        }

        val firstItemDescription =
            mockDaysList.days.first().weather.first().description.myCapitalize()

        composeTestRule.onNode(hasText(firstItemDescription)).assertIsDisplayed()

        composeTestRule.onNodeWithText(firstItemDescription).performClick()

        assert(clickedDay == mockDaysList.days.first())
    }
}

private fun createMockDay(
    dt: Long,
    tempMin: Double,
    tempMax: Double,
    description: String,
    index: Int
): DayBO {
    return DayBO(
        dt = dt,
        temp = TempBO(
            day = 0.0,
            min = tempMin,
            max = tempMax,
            night = 0.0,
            eve = 0.0,
            morn = 0.0
        ),
        weather = arrayListOf(
            WeatherBO(id = 500, main = "Test", description = description, icon = "01d")
        ),
        sunrise = 0, sunset = 0, moonrise = 0, moonset = 0, moonPhase = 0.0, summary = "",
        pressure = 0, humidity = 0, dewPoint = 0.0, windSpeed = 0.0, windDeg = 0,
        windGust = 0.0, clouds = 0, pop = 0.0, rain = 0.0, uvi = 0.0
    )
}

private val mockDaysList = DaysListBO(
    days = listOf(
        createMockDay(
            dt = 1782364800,
            tempMin = 10.0,
            tempMax = 20.0,
            description = "cloudy",
            index = 0
        ),
        createMockDay(
            dt = 1782451200,
            tempMin = 5.0,
            tempMax = 15.0,
            description = "rainy",
            index = 1
        )
    )
)
