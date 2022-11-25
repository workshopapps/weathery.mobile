package com.gear.weathery.dashboard.models

data class UITimeWeather(val id: Int, val main: String, val description: String, val date: String, val time: String, var highlighted: Boolean = false)