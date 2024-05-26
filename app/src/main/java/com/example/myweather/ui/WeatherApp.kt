package com.example.myweather.ui

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myweather.api.NetworkResponse
import com.example.myweather.data.LocationBeijing
import com.example.myweather.model.MyLatLng
import com.example.myweather.ui.screens.AddCityScreen
import com.example.myweather.ui.screens.CityList
import com.example.myweather.ui.screens.CityScreen
import com.example.myweather.ui.screens.MainScreen
import com.example.myweather.ui.screens.WeatherViewModel

enum class WeatherAppScreen {
    Main,
    City,
    AddCity
}

@Composable
fun WeatherApp(currentLatLag: MyLatLng?, viewModel: WeatherViewModel = viewModel(),
               navController: NavHostController = rememberNavController()) {

    val cityResult = viewModel.cityResult.observeAsState()
    val weatherNow = viewModel.weatherNowResult.observeAsState()
    val weather3Days = viewModel.weather3DaysResult.observeAsState()
//    val cityList = viewModel.cityList.observeAsState()
    val cityList by viewModel.cityList.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    Log.d("Mylocation", "currentLotLag: ${currentLatLag?.lat}, ${currentLatLag?.log}")
    viewModel.getCityList("${"%.2f".format(currentLatLag?.log)},${"%.2f".format(currentLatLag?.lat)}")
    Log.d("Mylocation", "${cityResult.value}")
    when (val cities = cityResult.value) {
        is NetworkResponse.Success -> {
            if (cities.data != null) {
                viewModel.setAsFirstCity(cities.data[0])
            }
        }
        is NetworkResponse.Error -> {}
        is NetworkResponse.Loading -> {}
        null -> {}
    }



    NavHost(navController = navController, startDestination = WeatherAppScreen.Main.name) {
        composable(WeatherAppScreen.Main.name) {
            MainScreen(
                location = currentLocation,
                weatherNow = weatherNow.value,
                weather3Days = weather3Days.value,
                onRefresh = {
                    viewModel.getWeatherNow(viewModel.currentLocation.value.id)
                    viewModel.getWeather3Days(viewModel.currentLocation.value.id)
                },
                onCityButtonClicked = { navController.navigate(WeatherAppScreen.City.name) }
            )
        }
        composable(WeatherAppScreen.City.name) {
            CityScreen(
                cityList = cityList,
                onDelete = { viewModel.deleteCity(it) },
                onSelect = {
                    viewModel.setCurrentLocation(it)
                    navController.navigate(WeatherAppScreen.Main.name)
                           },
                onAddCity = { navController.navigate(WeatherAppScreen.AddCity.name) }
            )
        }
        composable(WeatherAppScreen.AddCity.name) {
            AddCityScreen(
                cityResult = cityResult.value,
                onSearch = { viewModel.getCityList(it) },
                onSelect = {
                    viewModel.addCity(it)
                    viewModel.clearCityResult()
                    navController.navigate(WeatherAppScreen.City.name)
                           },
                onReturnButtonClicked = { navController.navigate(WeatherAppScreen.City.name) }
            )
        }
    }
}