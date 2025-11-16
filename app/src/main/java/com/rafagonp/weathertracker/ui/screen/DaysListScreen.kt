package com.rafagonp.weathertracker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rafagonp.weathertracker.R
import com.rafagonp.weathertracker.domain.model.DayBO
import com.rafagonp.weathertracker.domain.model.DaysListBO
import com.rafagonp.weathertracker.domain.model.Resource
import com.rafagonp.weathertracker.domain.model.Status
import com.rafagonp.weathertracker.domain.model.TempBO
import com.rafagonp.weathertracker.domain.model.WEATHER_ICONS_URL_BASE
import com.rafagonp.weathertracker.domain.model.WEATHER_ICONS_URL_EXTENSION
import com.rafagonp.weathertracker.domain.model.WeatherBO
import com.rafagonp.weathertracker.domain.model.myCapitalize
import com.rafagonp.weathertracker.domain.model.toDate
import com.rafagonp.weathertracker.domain.model.toDay
import com.rafagonp.weathertracker.ui.ERROR_TEXT_TAG
import com.rafagonp.weathertracker.ui.ITEM_ROW_TAG_PREFIX
import com.rafagonp.weathertracker.ui.LOADING_TEXT_TAG
import com.rafagonp.weathertracker.ui.SUCCESS_LIST_TAG
import com.rafagonp.weathertracker.viewmodels.DaysListViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlin.math.roundToInt

@Composable
fun DaysListScreen(
    viewModel: DaysListViewModel,
    onItemClick: (DayBO) -> Unit,
    modifier : Modifier
) {
    val daysListFlow: SharedFlow<Resource<DaysListBO>> = viewModel.daysListFlow
    val resource: Resource<DaysListBO> =
        daysListFlow.collectAsStateWithLifecycle(Resource.loading(DaysListBO(mutableListOf()))).value

    DaysListContent(
        resource,
        onItemClick
    )
}

@Composable
fun DaysListContent(
    daysListResource: Resource<DaysListBO>,
    onItemClick: (DayBO) -> Unit,
    modifier: Modifier = Modifier
) {
    when (daysListResource.status) {
        Status.LOADING -> {
            Text(
                text = stringResource(R.string.loading),
                modifier = modifier
                    .padding(top = 16.dp)
                    .testTag(LOADING_TEXT_TAG)
            )
        }

        Status.ERROR -> {
            Text(
                text = daysListResource.message ?: stringResource(R.string.error),
                modifier = modifier.testTag(ERROR_TEXT_TAG)
            )
        }

        Status.SUCCESS -> {
            val daysListValues = (daysListResource.data as DaysListBO).days
            Card(
                modifier = modifier.testTag(SUCCESS_LIST_TAG)
            ) {
                LazyColumn(
                    modifier = modifier.testTag(ITEM_ROW_TAG_PREFIX)
                ){
                    items(daysListValues.count()) { index ->
                        val day = daysListValues[index]
                        DaysListItem(
                            day,
                            modifier = Modifier
                                .background(Color.White)
                                .clickable {
                                    onItemClick(day)
                                }
                        )

                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DaysListItem(
    day: DayBO,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = modifier.weight(0.75f),
            ) {
                Text(
                    text = day.dt.toDay(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = day.dt.toDate(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
            }
            Row(
                modifier = modifier.weight(1.5f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                GlideImage(
                    model = WEATHER_ICONS_URL_BASE + day.weather[0].icon + WEATHER_ICONS_URL_EXTENSION,
                    contentDescription = day.weather[0].description,
                    modifier = modifier.size(32.dp)
                )
                Spacer(modifier = modifier.width(dimensionResource(R.dimen.spacer_vertical_small)))
                Text(
                    text = day.weather[0].description.myCapitalize(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Row(
                modifier = modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = String.format(stringResource(R.string.temp), day.temp.min.roundToInt()),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    color = Color.Blue
                )
                Text(
                    text = stringResource(R.string.temp_divider),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    color = Color.Black
                )
                Text(
                    text = String.format(stringResource(R.string.temp), day.temp.max.roundToInt()),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    color = Color.Red
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacer_horizontal_medium)),
            color = Color.LightGray.copy(alpha = 0.5f),
            thickness = 1.dp
        )
    }
}

@Preview
@Composable
fun DaysListScreenPreview() {
    DaysListContent(
        daysListResource = Resource.success(
            DaysListBO(
                listOf(
                    DayBO(
                        dt = 17823649817,
                        sunrise = 1763103573,
                        sunset = 1763139485,
                        moonrise = 1763139485,
                        moonset = 1763139485,
                        moonPhase = 0.75,
                        summary = "Expect a day of partly cloudy with rain",
                        temp = TempBO(
                            day = 11.8,
                            min = 10.83,
                            max = 13.98,
                            night = 10.83,
                            eve = 12.31,
                            morn = 11.14
                        ),
                        pressure = 1000,
                        humidity = 75,
                        dewPoint = 8.05,
                        windSpeed = 11.55,
                        windDeg = 170,
                        windGust = 19.25,
                        weather = arrayListOf(
                            WeatherBO(
                                id = 500,
                                main = "Rain",
                                description = "light rain",
                                icon = "09d"
                            )
                        ),
                        clouds = 87,
                        pop = 1.0,
                        rain = 12.68,
                        uvi = 2.1
                    ),
                    DayBO(
                        dt = 178232354817,
                        sunrise = 1763113573,
                        sunset = 1763432185,
                        moonrise = 17631234485,
                        moonset = 1763123485,
                        moonPhase = 0.81,
                        summary = "Expect a day of rainy mans aleluya",
                        temp = TempBO(
                            day = 15.7,
                            min = 8.25,
                            max = 15.7,
                            night = 10.83,
                            eve = 12.31,
                            morn = 11.14
                        ),
                        pressure = 1025,
                        humidity = 80,
                        dewPoint = 8.15,
                        windSpeed = 12.55,
                        windDeg = 180,
                        windGust = 19.23,
                        weather = arrayListOf(
                            WeatherBO(
                                id = 500,
                                main = "Clouds",
                                description = "Cloudy",
                                icon = "03d"
                            )
                        ),
                        clouds = 85,
                        pop = 1.5,
                        rain = 10.68,
                        uvi = 2.5
                    )
                )
            )
        ),
        { },
        Modifier
    )
}