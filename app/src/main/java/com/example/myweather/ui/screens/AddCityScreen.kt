package com.example.myweather.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myweather.api.NetworkResponse
import com.example.myweather.api.cityLookup.Location
import com.example.myweather.R

@Composable
fun AddCityScreen(
    cityResult: NetworkResponse<List<Location>>?,
    onSearch: (String) -> Unit,
    onSelect: (Location) -> Unit,
    onReturnButtonClicked: () -> Unit
) {
    var city by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { onReturnButtonClicked() }) {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Return to city list")
            }
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = city,
                onValueChange = {
                    city = it
                    onSearch(city)
                    Log.d("AddCityScreen", "City: $city")
                },
                label = {
                    Text(stringResource(id = R.string.search_for_location))
                }
            )
        }

            when (cityResult) {
                is NetworkResponse.Error -> {
                    Log.i("AddCityScreen", "Error: ${cityResult.message}")
                }
                NetworkResponse.Loading -> {}
                is NetworkResponse.Success -> {
                    CityList(data = cityResult.data) {
                        onSelect(it)
                    }
                }
                null -> {}
            }
    }
}

@Composable
fun CityList(data: List<Location>, onItemClicked: (Location) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        data.forEach {
            CityItem(city = it.name) {
                Log.d("CityList", "City: ${it.name}")
                onItemClicked(it)
            }
        }
    }
}

@Composable
fun CityItem(city: String, onClick: () -> Unit) {
    Text(
        text = city,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    )
}

@Preview
@Composable
fun AddCityScreenPreview() {
    AddCityScreen(
        cityResult = null,
        onSearch = {},
        onSelect = {},
        onReturnButtonClicked = {}
    )
}