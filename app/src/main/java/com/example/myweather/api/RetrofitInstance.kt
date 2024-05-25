package com.example.myweather.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val weatherUrl = "https://devapi.qweather.com/"
    private const val geoUrl = "https://geoapi.qweather.com/"

    private fun getWeatherInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(weatherUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getGeoInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(geoUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val weatherApi: WeatherApi = getWeatherInstance().create(WeatherApi::class.java)
    val geoApi: GeoApi = getGeoInstance().create(GeoApi::class.java)
}