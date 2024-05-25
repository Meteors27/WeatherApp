package com.example.myweather.ui.screens

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.api.Constant
import com.example.myweather.api.NetworkResponse
import com.example.myweather.api.RetrofitInstance
import com.example.myweather.api.cityLookup.Location
import com.example.myweather.api.weather3Days.Weather3Days
import com.example.myweather.api.weatherNow.WeatherNow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception

class WeatherViewModel : ViewModel() {
    private val weatherApi = RetrofitInstance.weatherApi
    private val geoApi = RetrofitInstance.geoApi

    private val _weatherNowResult = MutableLiveData<NetworkResponse<WeatherNow>>()
    val weatherNowResult : LiveData<NetworkResponse<WeatherNow>> = _weatherNowResult

    private val _weather3DaysResult = MutableLiveData<NetworkResponse<Weather3Days>>()
    val weather3DaysResult : LiveData<NetworkResponse<Weather3Days>> = _weather3DaysResult

    private val _cityResult = MutableLiveData<NetworkResponse<List<Location>>>()
    val cityResult : LiveData<NetworkResponse<List<Location>>> = _cityResult

//    private val _cityList = MutableLiveData<List<Location>>()
//    val cityList : LiveData<List<Location>> = _cityList

    private val _cityList = MutableStateFlow(listOf<Location>())
    val cityList : StateFlow<List<Location>> = _cityList.asStateFlow()

    fun getWeatherNow(locationId : String) {
        _weatherNowResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            val response = weatherApi.getWeatherNow(Constant.apiKey, locationId)
            if (response.isSuccessful) {
                try {
                    val realtimeWeather = response.body()
                    if (realtimeWeather != null) {
                        response.body()?.let {
                            _weatherNowResult.value = NetworkResponse.Success(it)
                        }
                    } else {
                        _weatherNowResult.value =
                            NetworkResponse.Error("Failed to load weather data")
                    }
                }
                catch (e: Exception) {
                    _weatherNowResult.value = NetworkResponse.Error("Failed to load weather data")
                }
            }
        }
    }

    fun getWeather3Days(locationId : String) {
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather3Days(Constant.apiKey, locationId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weather3DaysResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weather3DaysResult.value = NetworkResponse.Error("Failed to load weather data")
                }
            } catch (e: Exception) {
                _weather3DaysResult.value = NetworkResponse.Error("Failed to load weather data")
            }

        }
    }

    fun getCityList(location : String) {
        viewModelScope.launch {
            try {
                val response = geoApi.getCity(Constant.apiKey, location)
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.location != null) {
                            _cityResult.value = NetworkResponse.Success(it.location)
                        }
                        else {
                            _cityResult.value = NetworkResponse.Error("Data is null")

                        }
                    }
                } else {
                    _cityResult.value = NetworkResponse.Error("Failed to load city data")
                }
            }
            catch (e: Exception) {
                _cityResult.value = NetworkResponse.Error("Failed to load city data")
            }
        }
    }

    fun clearCityResult() {
        _cityResult.value = null
    }

    fun addCity(location: Location) {
        _cityList.value += location
        Log.d("AddCityList", "City list: ${_cityList.value})")
    }

    fun deleteCity(locationId: String) {
        _cityList.value = _cityList.value.filter { it.id != locationId }
    }
}