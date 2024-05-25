package com.example.myweather.api.weatherNow

data class WeatherNow(
    val code: String,
    val fxLink: String,
    val now: Now,
    val refer: Refer?,
    val updateTime: String
)