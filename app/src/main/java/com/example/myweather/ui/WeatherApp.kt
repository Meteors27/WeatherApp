package com.example.myweather.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myweather.ui.screens.AddCityScreen
import com.example.myweather.ui.screens.CityScreen
import com.example.myweather.ui.screens.MainScreen
import com.example.myweather.ui.screens.WeatherViewModel

enum class WeatherAppScreen {
    Main,
    City,
    AddCity
}

@Composable
fun WeatherApp(viewModel: WeatherViewModel = viewModel(),
               navController: NavHostController = rememberNavController()) {

    val cityResult = viewModel.cityResult.observeAsState()
    val weatherNow = viewModel.weatherNowResult.observeAsState()
    val weather3Days = viewModel.weather3DaysResult.observeAsState()
//    val cityList = viewModel.cityList.observeAsState()
    val cityList by viewModel.cityList.collectAsState()

    NavHost(navController = navController, startDestination = WeatherAppScreen.Main.name) {
        composable(WeatherAppScreen.Main.name) {
            MainScreen(
                weatherNow = weatherNow.value,
                weather3Days = weather3Days.value,
                onCityButtonClicked = { navController.navigate(WeatherAppScreen.City.name) }
            )
        }
        composable(WeatherAppScreen.City.name) {
            CityScreen(
                cityList = cityList,
                onDelete = { viewModel.deleteCity(it) },
                onSelect = { navController.navigate(WeatherAppScreen.Main.name) },
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