package com.rafagonp.weathertracker.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WindPower
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rafagonp.weathertracker.R
import com.rafagonp.weathertracker.domain.model.DayBO
import com.rafagonp.weathertracker.domain.model.TempBO
import com.rafagonp.weathertracker.domain.model.WEATHER_ICONS_URL_BASE
import com.rafagonp.weathertracker.domain.model.WEATHER_ICONS_URL_EXTENSION
import com.rafagonp.weathertracker.domain.model.WeatherBO
import com.rafagonp.weathertracker.domain.model.myCapitalize
import com.rafagonp.weathertracker.domain.model.toDate
import com.rafagonp.weathertracker.domain.model.toDay
import com.rafagonp.weathertracker.domain.model.toTime
import kotlin.math.roundToInt

@Composable
fun DayDetailScreen(
    day: DayBO,
    modifier : Modifier
) {
    DayDetailContent(
        day
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DayDetailContent(day: DayBO) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = day.dt.toDay(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = day.dt.toDate(),
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_vertical_medium)))

            GlideImage(
                model = WEATHER_ICONS_URL_BASE + day.weather[0].icon + WEATHER_ICONS_URL_EXTENSION,
                contentDescription = day.weather[0].description,
                modifier = Modifier.size(96.dp)
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_vertical_small)))
            Text(
                text = day.summary.myCapitalize(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_vertical_medium)))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = String.format(stringResource(R.string.max_temp), day.temp.max.roundToInt()),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacer_horizontal_big)))
                Text(
                    text = String.format(stringResource(R.string.min_temp), day.temp.min.roundToInt()),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacer_vertical_big)))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                DetailItem(
                    label = stringResource(R.string.humidity),
                    value = "${day.humidity}%",
                    icon = {
                        Icon(
                            Icons.Filled.WaterDrop,
                            contentDescription = null,
                            tint = Color.Blue
                        )
                    }
                )
                DetailItem(
                    label = stringResource(R.string.wind),
                    value = "${day.windSpeed.roundToInt()} km/h",
                    icon = {
                        Icon(
                            Icons.Filled.WindPower,
                            contentDescription = null,
                            tint = Color.Green
                        )
                    }
                )
                DetailItem(
                    label = stringResource(R.string.rain),
                    value = "${day.rain.roundToInt()}%",
                    icon = {
                        Icon(
                            Icons.Filled.CloudQueue,
                            contentDescription = null,
                            tint = Color.Blue
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_vertical_big)))

            // Segunda Fila de Detalles (Sol y UV)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                DetailItem(
                    label = stringResource(R.string.dawn),
                    value = day.sunrise.toTime(),
                    icon = {
                        Icon(
                            Icons.Filled.WbSunny,
                            contentDescription = null,
                            tint = colorResource(R.color.orange)
                        )
                    }
                )
                DetailItem(
                    label = stringResource(R.string.dusk),
                    value = day.sunset.toTime(),
                    icon = {
                        Icon(
                            Icons.Filled.WbSunny,
                            contentDescription = null,
                            tint = Color.Yellow
                        )
                    }
                )
                DetailItem(
                    label = stringResource(R.string.uv),
                    value = "${day.uvi}",
                    icon = {
                        Icon(
                            Icons.Filled.WbSunny,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun DetailItem(
    label: String,
    value: String,
    icon: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.widthIn(min = 80.dp)
    ) {
        icon()
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_vertical_very_small)))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Preview
@Composable
fun DayDetailScreenPreview() {
    DayDetailContent(
        DayBO(
            dt = 178232354817,
            sunrise = 1763113573,
            sunset = 1763432185,
            moonrise = 17631234485,
            moonset = 1763123485,
            moonPhase = 0.81,
            summary = "Expect a day of rainy men aleluya",
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
}