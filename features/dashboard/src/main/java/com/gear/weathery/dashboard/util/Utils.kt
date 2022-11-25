package com.gear.weathery.dashboard.util

import com.gear.weathery.dashboard.models.NWTimeWeather
import com.gear.weathery.dashboard.models.TimeWeather
import com.gear.weathery.dashboard.models.UITimeWeather


fun List<NWTimeWeather>.toTimeWeathers(): List<TimeWeather>{
    val timeWeatherList = mutableListOf<TimeWeather>()
    for ((id, nWTimeWeather) in this.withIndex()){
        timeWeatherList.add(TimeWeather(id, nWTimeWeather.main, nWTimeWeather.description, nWTimeWeather.date, nWTimeWeather.time))
    }
    return timeWeatherList
}

fun List<TimeWeather>.toUITimeWeathers(): List<UITimeWeather>{
    val uiTimeWeatherList = mutableListOf<UITimeWeather>()
    for (uITimeWeather in this){
        uiTimeWeatherList.add(UITimeWeather(uITimeWeather.id, uITimeWeather.main, uITimeWeather.description, uITimeWeather.date, uITimeWeather.time))
    }
    return uiTimeWeatherList
}