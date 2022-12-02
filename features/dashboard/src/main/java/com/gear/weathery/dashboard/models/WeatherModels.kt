package com.gear.weathery.dashboard.models

import java.text.DateFormat
import java.util.*

data class HourWeather(val main: String, val risk: String, val timeInMillis: Long)

class DayWeather{
    var state: String = ""
    var country: String = ""
    lateinit var currentWeather: WeatherCondition
    var timeLine: List<HourWeather> = mutableListOf()
}

data class WeatherCondition(val main: String, val risk: String, val timeInMillis: Long, val endTimeTimeInMillis: Long)

fun getTimeForDisplay(timeInMillis: Long): String{
    val format = DateFormat.getTimeInstance(DateFormat.SHORT)
    val date = Calendar.getInstance().also { it.timeInMillis = timeInMillis }
    return format.format(date.time)
}

fun getAmPmTime(timeInMillis: Long): Pair<String, String>{
    val date = Calendar.getInstance().also { it.timeInMillis = timeInMillis }
    val twelveHourTime = if(date.get(Calendar.HOUR) == 0) 12 else date.get(Calendar.HOUR)
    val amPmValue = date.get(Calendar.AM_PM)
    val amPmStringValue = if (amPmValue == Calendar.AM) "AM" else "PM"
    return Pair(twelveHourTime.toString(), amPmStringValue)
}