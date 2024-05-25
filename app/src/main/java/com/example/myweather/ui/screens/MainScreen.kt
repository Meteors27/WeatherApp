package com.example.myweather.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myweather.api.NetworkResponse
import com.example.myweather.api.weather3Days.Weather3Days
import com.example.myweather.api.weatherNow.WeatherNow

@Composable
fun MainScreen(
    weatherNow: NetworkResponse<WeatherNow>?,
    weather3Days: NetworkResponse<Weather3Days>?,
    onCityButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom

        ) {
            Icon(imageVector = Icons.Default.LocationOn,
                contentDescription = "Location icon",
                modifier = Modifier.size(40.dp))
        }
        IconButton(onClick = { onCityButtonClicked() }) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "City list"
            )
        }
        Text(weatherNow.toString())
        Text(weather3Days.toString())
    }
}