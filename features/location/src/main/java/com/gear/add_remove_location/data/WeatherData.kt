package com.gear.add_remove_location.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import com.gear.add_remove_location.R
import com.gear.add_remove_location.presentation.ui.theme.Clear
import com.gear.add_remove_location.presentation.ui.theme.ClearNight
import com.gear.add_remove_location.presentation.ui.theme.Cloudy
import com.gear.add_remove_location.presentation.ui.theme.Rainy

//Test data to be amended in implementation phase
data class WeatherData(
    val location: String,
    val weather: String,
    val time: String
)

fun getWeather(): MutableList<WeatherData> {
    return mutableStateListOf(
        WeatherData(
            location = "owerri",
            time = "2.00PM",
            weather = "Rainy"
        ),
        WeatherData(
            location = "ibadan",
            time = "12.00PM",
            weather = "Cloudy"
        ),
        WeatherData(
            location = "ghana",
            time = "4.00PM",
            weather = "Clear"
        ),
        WeatherData(
            location = "Kenya",
            time = "10.00PM",
            weather = "Clear"
        ),
    )
}

fun getImageRes(location: String): Int {
    return when (location) {
        "Kenya" -> R.drawable.location_ic_clear_night
        "ghana" -> R.drawable.location_ic_sunny
        "ibadan" -> R.drawable.location_ic_cloudy
        "owerri" -> R.drawable.location_ic_scattered_thunderstorm
        else -> {
            R.drawable.location_ic_sunny
        }
    }
}

fun getColor(location: String): Color {
    return when (location) {
        "Kenya" -> ClearNight
        "ghana" -> Clear
        "ibadan" -> Cloudy
        "owerri" -> Rainy
        else -> { Clear }
    }
}