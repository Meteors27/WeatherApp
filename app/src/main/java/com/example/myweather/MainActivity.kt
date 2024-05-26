package com.example.myweather

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.core.content.ContextCompat
import com.example.myweather.constant.Const.Companion.permissions
import com.example.myweather.model.MyLatLng
import com.example.myweather.ui.WeatherApp
import com.example.myweather.ui.theme.MyWeatherTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.coroutineScope

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var locationRequired: Boolean = false

    override fun onResume() {
        super.onResume()
        if (locationRequired) startLocationUpdate();
    }

    override fun onPause() {
        super.onPause()
        locationCallback?.let { fusedLocationProviderClient?.removeLocationUpdates(it) }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdate() {
        locationCallback?.let {
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 10000
            )
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(3000)
                .setMaxUpdateDelayMillis(100)
                .build()

            fusedLocationProviderClient?.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLocationClient()

        setContent {
            var currentLatLng by remember {
                mutableStateOf(MyLatLng(0.0, 0.0))
            }

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        currentLatLng = MyLatLng(location.latitude, location.longitude)
                    }
                }
            }

            MyWeatherTheme {
                LocationScreen(context = this@MainActivity, currentLatLng = currentLatLng)
            }
        }
    }
    @Composable
    private fun LocationScreen(context: Context, currentLatLng: MyLatLng) {

        val launcherMultiplePermissions = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionMap ->
            val areGranted = permissionMap.values.reduce{
                    accepted, next -> accepted && next
            }
            if (areGranted) {
                locationRequired = true
                startLocationUpdate()
                Log.d("Permission Granted", "Location permission granted")
            } else {
                Log.d("Permission Denied", "Location permission denied")
            }
        }

        LaunchedEffect(
            key1 = currentLatLng,
            block = {
                coroutineScope {
                    if (permissions.all {
                            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
                        }) {
                        startLocationUpdate()
                    } else {
                        launcherMultiplePermissions.launch(permissions)
                    }
                }
            }
        )
//        Text("${currentLatLng.lat},${currentLatLng.log}")
        WeatherApp(currentLatLag = currentLatLng)
    }

    private fun initLocationClient() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }
}
