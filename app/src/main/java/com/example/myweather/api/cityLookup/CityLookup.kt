package com.example.myweather.api.cityLookup

data class CityLookup(
    val code: String,
    val location: List<Location>,
    val refer: Refer
)