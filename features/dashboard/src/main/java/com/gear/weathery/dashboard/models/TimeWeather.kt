package com.gear.weathery.dashboard.models

data class TimeWeather(val id: Int, val main: String, val description: String, val date: String, val time: String)

data class NWTimeWeather(val main: String, val description: String, val date: String, val time: String)