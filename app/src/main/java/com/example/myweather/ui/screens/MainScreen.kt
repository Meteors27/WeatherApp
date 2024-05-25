package com.example.myweather.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@Composable
fun MainScreen(
    location: Location,
    weatherNow: NetworkResponse<WeatherNow>?,
    weather3Days: NetworkResponse<Weather3Days>?,
    onCityButtonClicked: () -> Unit,
    onRefresh: () -> Unit
) {
    // TODO
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

        Text(location.name)
        Text(location.adm1 + ", " + location.adm2)

        when(weatherNow) {
            is NetworkResponse.Success -> {
                WeatherNowItem(weatherNow.data)
            }
            is NetworkResponse.Error -> {
                Text("Error: ${weatherNow.message}")
            }
            is NetworkResponse.Loading -> {
                Text("Loading...")
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
                Text("Error: ${weather3Days.message}")
            }
            is NetworkResponse.Loading -> {
                Text("Loading...")
            }
            null -> {}
        }
    }
}

@Composable
fun WeatherNowItem(weatherNow: WeatherNow) {
    // icon from xml
    QweatherIcon(id = 101)
    Text(weatherNow.now.temp + "°C")
    Text(weatherNow.now.text)
    Text(weatherNow.now.windDir + " " + weatherNow.now.windScale + "级")
    Text("湿度: " + weatherNow.now.humidity + "%")
    Text("降水量: " + weatherNow.now.precip + "mm")
    Text("气压: " + weatherNow.now.pressure + "hPa")
    Text("能见度: " + weatherNow.now.vis + "km")
    Text("云量: " + weatherNow.now.cloud + "%")
    Text("露点: " + weatherNow.now.dew + "°C")
    Text("体感温度: " + weatherNow.now.feelsLike + "°C")
    Text("更新时间: " + weatherNow.updateTime)
}

@Composable
fun WeatherDailyItem(daily: Daily) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(daily.fxDate)
        // icon
        Text(daily.tempMin + " / " + daily.tempMax)

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