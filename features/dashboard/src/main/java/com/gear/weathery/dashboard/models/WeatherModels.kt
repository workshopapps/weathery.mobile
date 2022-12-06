package com.gear.weathery.dashboard.models

import com.gear.weathery.dashboard.ui.FAILED
import com.gear.weathery.dashboard.ui.PASSED
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

data class TimelineWeather(val main: String, val risk: String, val timeInMillis: Long)

class DayWeather{
    var state: String = ""
    var country: String = ""
    lateinit var currentWeather: WeatherCondition
    var timeLine: List<TimelineWeather> = mutableListOf()
}

data class WeatherCondition(val state: String, val country: String, val main: String, val risk: String, val timeInMillis: Long, val endTimeTimeInMillis: Long)

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

fun getDateForDisplay(timeInMillis: Long): Pair<String, String>{
    val calendar = Calendar.getInstance().also { it.timeInMillis = timeInMillis }
    val dayFormat = SimpleDateFormat("EEE")
    val day = dayFormat.format(calendar.time).uppercase()

    val dateFormat = SimpleDateFormat("d'/'MM")
    val date = dateFormat.format(calendar.time)

    return Pair(day, date)
}

sealed class DayWeatherResponse(val status: Int, val payLoad: DayWeather?){
    class SuccessDayWeatherResponse(payLoad: DayWeather): DayWeatherResponse(PASSED, payLoad)
    class FailureDayWeatherResponse(): DayWeatherResponse(FAILED, null)
}

sealed class TimelineResponse(val status: Int, val payLoad: List<TimelineWeather>?){
    class SuccessTimelineResponse(payLoad: List<TimelineWeather>): TimelineResponse(PASSED, payLoad)
    class FailureTimelineResponse(): TimelineResponse(FAILED, null)
}