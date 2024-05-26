package com.example.myweather.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myweather.R
import com.example.myweather.api.NetworkResponse
import com.example.myweather.api.cityLookup.Location
import com.example.myweather.api.weather3Days.Daily
import com.example.myweather.api.weather3Days.Weather3Days
import com.example.myweather.api.weatherNow.Now
import com.example.myweather.api.weatherNow.Refer
import com.example.myweather.api.weatherNow.WeatherNow
import com.example.myweather.data.LocationBeijing
import com.example.myweather.data.QweatherIcon
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun MainScreen(
    location: Location,
    weatherNow: NetworkResponse<WeatherNow>?,
    weather3Days: NetworkResponse<Weather3Days>?,
    onCityButtonClicked: () -> Unit,
    onRefresh: () -> Unit
) {
    LaunchedEffect(Unit) {
        onRefresh()
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom

        ) {
            IconButton(onClick = { onCityButtonClicked() }) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "City list"
                )
            }
        }

        Text(location.name,
            style = MaterialTheme.typography.titleLarge
        )
        Text(location.adm1 + ", " + location.adm2,
            style = MaterialTheme.typography.titleSmall
        )

        when(weatherNow) {
            is NetworkResponse.Success -> {
                WeatherNowItem(weatherNow.data)
            }
            is NetworkResponse.Error -> {
                Log.d("MainScreen", "Error: ${weatherNow.message}")
            }
            is NetworkResponse.Loading -> {
                Log.i("MainScreen", "Loading...")
            }
            null -> {}
        }

        when(weather3Days) {
            is NetworkResponse.Success -> {
                Column {
                    weather3Days.data.daily.forEach {
                        WeatherDailyItem(it)
                    }
                }
            }
            is NetworkResponse.Error -> {
                Log.d("MainScreen", "Error: ${weather3Days.message}")
            }
            is NetworkResponse.Loading -> {
                Log.i("MainScreen", "Loading...")
            }
            null -> {}
        }
    }
}

@Composable
fun DisplayFormattedDate(dateString: String) {
    val formattedDate = formatDateTime(dateString)
    Text(text = formattedDate)
}

fun formatDateTime(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mmXXX", Locale.getDefault())
    val outputDateFormat = SimpleDateFormat("MM/dd, HH:mm", Locale.getDefault())
    val outputTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val outputYearFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    val date = inputFormat.parse(dateString)
    val currentDate = Calendar.getInstance().time

    val inputCalendar = Calendar.getInstance().apply { time = date }
    val currentCalendar = Calendar.getInstance().apply { time = currentDate }

    return if (inputCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
        inputCalendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR)) {
        outputTimeFormat.format(date)
    } else if (inputCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)) {
        outputDateFormat.format(date)
    } else {
        outputYearFormat.format(date)
    }
}

@Composable
fun WeatherNowItem(weatherNow: WeatherNow) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(bottom = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        DisplayFormattedDate(dateString = weatherNow.now.obsTime)
        QweatherIcon(
            id = weatherNow.now.icon.toInt(),
            modifier = Modifier
                .size(120.dp)
                .padding(top = 60.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.width(40.dp))
            Text(
                weatherNow.now.temp,
                fontSize = 60.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Text(
                "°",
                fontSize = 60.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                modifier = Modifier.width(40.dp)
            )
        }

        Text(weatherNow.now.text,
            modifier = Modifier.padding(top = 10.dp, bottom = 30.dp))

        Row {
            WeatherDetail(
                title = R.string.wind_speed,
                value = weatherNow.now.windSpeed + " km/h",
                iconId = 2208
            )
            Spacer(modifier = Modifier.width(20.dp))
            WeatherDetail(
                title = R.string.humidity,
                value = weatherNow.now.humidity + " %",
                iconId = 399
            )
            Spacer(modifier = Modifier.width(20.dp))
            WeatherDetail(
                title = R.string.pressure,
                value = weatherNow.now.pressure + " hPa",
                iconId = 2210
            )
        }
    }
}

@Composable
fun WeatherDetail(title: Int, value: String, iconId: Int) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(id = title),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            QweatherIcon(
                id = iconId,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(value)

        }
    }
}

@Composable
fun WeatherDailyItem(daily: Daily) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(daily.fxDate)
        val calendar = Calendar.getInstance().apply { time = date }
        val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
        val dayOfWeek = dayOfWeekFormat.format(date)
        // if is today, show "Today"
        if (calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) &&
            calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
            Text(stringResource(id = R.string.today))
        } else {
            when(dayOfWeek) {
                "Monday" -> Text(stringResource(id = R.string.monday))
                "Tuesday" -> Text(stringResource(id = R.string.tuesday))
                "Wednesday" -> Text(stringResource(id = R.string.wednesday))
                "Thursday" -> Text(stringResource(id = R.string.thursday))
                "Friday" -> Text(stringResource(id = R.string.friday))
                "Saturday" -> Text(stringResource(id = R.string.saturday))
                "Sunday" -> Text(stringResource(id = R.string.sunday))
                else -> { Text(dayOfWeek) }
            }
        }
        QweatherIcon(
            id = daily.iconDay.toInt(),
        )
        Row{
            Text(daily.tempMax + "°",
                modifier = Modifier
                    .width(30.dp)
                    .wrapContentWidth(Alignment.End)
            )
            Text(daily.tempMin + "°",
                modifier = Modifier
                    .width(30.dp)
                    .wrapContentWidth(Alignment.End)
            )
        }

    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        location = LocationBeijing,
        weatherNow = NetworkResponse.Success(
            WeatherNow(
                now = Now(
                    obsTime = "2020-06-30T21:40+08:00",
                    temp = "24",
                    feelsLike = "26",
                    icon = "101",
                    text = "多云",
                    wind360 = "123",
                    windDir = "东南风",
                    windScale = "1",
                    windSpeed = "3",
                    humidity = "72",
                    precip = "0.0",
                    pressure = "1003",
                    vis = "16",
                    cloud = "10",
                    dew = "21"
                ),
                updateTime = "2020-06-30T22:00+08:00",
                fxLink = "http://hfx.link/2ax1",
                code = "200",
                refer = null
            )
        ),
        weather3Days = NetworkResponse.Success(
            Weather3Days(
                daily = listOf(
                    Daily(
                        fxDate = "2021-11-15",
                        sunrise = "06:58",
                        sunset = "16:59",
                        moonrise = "15:16",
                        moonset = "03:40",
                        moonPhase = "盈凸月",
                        moonPhaseIcon = "803",
                        tempMax = "12",
                        tempMin = "-1",
                        iconDay = "101",
                        textDay = "多云",
                        iconNight = "150",
                        textNight = "晴",
                        wind360Day = "45",
                        windDirDay = "东北风",
                        windScaleDay = "1-2",
                        windSpeedDay = "3",
                        wind360Night = "0",
                        windDirNight = "北风",
                        windScaleNight = "1-2",
                        windSpeedNight = "3",
                        humidity = "65",
                        precip = "0.0",
                        pressure = "1020",
                        vis = "25",
                        cloud = "4",
                        uvIndex = "3"
                    ),
                    Daily(
                        fxDate = "2021-11-16",
                        sunrise = "07:00",
                        sunset = "16:58",
                        moonrise = "15:38",
                        moonset = "04:40",
                        moonPhase = "盈凸月",
                        moonPhaseIcon = "803",
                        tempMax = "13",
                        tempMin = "0",
                        iconDay = "100",
                        textDay = "晴",
                        iconNight = "101",
                        textNight = "多云",
                        wind360Day = "225",
                        windDirDay = "西南风",
                        windScaleDay = "1-2",
                        windSpeedDay = "3",
                        wind360Night = "225",
                        windDirNight = "西南风",
                        windScaleNight = "1-2",
                        windSpeedNight = "3",
                        humidity = "74",
                        precip = "0.0",
                        pressure = "1016",
                        vis = "25",
                        cloud = "1",
                        uvIndex = "3"
                    ),
                    Daily(
                        fxDate = "2021-11-17",
                        sunrise = "07:01",
                        sunset = "16:57",
                        moonrise = "16:01",
                        moonset = "05:41",
                        moonPhase = "盈凸月",
                        moonPhaseIcon = "803",
                        tempMax = "13",
                        tempMin = "0",
                        iconDay = "100",
                        textDay = "晴",
                        iconNight = "150",
                        textNight = "晴",
                        wind360Day = "225",
                        windDirDay = "西南风",
                        windScaleDay = "1-2",
                        windSpeedDay = "3",
                        wind360Night = "225",
                        windDirNight = "西南风",
                        windScaleNight = "1-2",
                        windSpeedNight = "3",
                        humidity = "56",
                        precip = "0.0",
                        pressure = "1009",
                        vis = "25",
                        cloud = "0",
                        uvIndex = "3"
                    ),
                ),
                updateTime = "2021-11-15T16:35+08:00",
                fxLink = "http://hfx.link/2ax1",
                code = "200",
                refer = null
            )
        ),
        onCityButtonClicked = {},
        onRefresh = {}
    )
}

@Preview
@Composable
fun MainScreenPreviewError() {
    MainScreen(
        location = LocationBeijing,
        weatherNow = NetworkResponse.Error("Error: Network error"),
        weather3Days = NetworkResponse.Error("Error: Network error"),
        onCityButtonClicked = {},
        onRefresh = {}
    )
}