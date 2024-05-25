package com.example.myweather.api

import com.example.myweather.api.cityLookup.CityLookup
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoApi {
    @GET("/v2/city/lookup")
    suspend fun getCity(
        @Query("key") apiKey : String,
        @Query("location") location : String
    ) : Response<CityLookup>
}