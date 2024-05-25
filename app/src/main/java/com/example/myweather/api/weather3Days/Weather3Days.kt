package com.example.myweather.api.weather3Days

data class Weather3Days(
    val code: String,
    val daily: List<Daily>,
    val fxLink: String,
    val refer: Refer?,
    val updateTime: String
)