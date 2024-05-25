package com.example.myweather.api
import com.example.myweather.api.weather3Days.Weather3Days
import com.example.myweather.api.weatherNow.WeatherNow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/v7/weather/now")
    suspend fun getWeatherNow(
        @Query("key") apiKey : String,
        @Query("location") locationId : String
    ) : Response<WeatherNow>

    @GET("/v7/weather/3d")
    suspend fun getWeather3Days(
        @Query("key") apiKey : String,
        @Query("location") locationId : String
    ) : Response<Weather3Days>
}