package com.gear.weathery.dashboard.repository

import com.gear.weathery.dashboard.models.DayWeather
import com.gear.weathery.dashboard.models.TimelineWeather
import com.gear.weathery.dashboard.models.WeatherCondition
import com.gear.weathery.dashboard.network.GeoCodingNetworkApi
import com.gear.weathery.dashboard.network.NetworkApi
import org.json.JSONArray
import org.json.JSONObject
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Calendar

const val NONE = "none"

object WeatherRepo {

    suspend fun getWeatherForToday(lat: Double, long: Double): DayWeather {
        return try {
            getWeatherFromNetwork(lat, long)
        } catch (e: Exception) {
            generateMockDayWeather(lat, long)
        }
    }

    suspend fun getTomorrowWeatherTimeline(lat: Double, long: Double): List<TimelineWeather> {
        return try {
            getWeatherFromNetworkForTomorrow(lat, long)
        } catch (e: Exception) {
            generateMockWeatherTimeline()
        }
    }

    suspend fun getThisWeekTimeline(lat: Double, long: Double): List<TimelineWeather> {
        return try {
            getWeatherFromNetworkForThisWeek(lat, long)
        } catch (e: Exception) {
            generateDailyWeatherTimeline(Calendar.getInstance())
        }
    }

    private val weatherConditions = listOf(
        "Rain showers",
        "Rain",
        "Thunderstorm",
        "Clear sky",
        "Scattered clouds",
        "Few clouds",
        "Broken clouds",
        "Fog and depositing rime fog",
        "Drizzle",
        "Freezing Drizzle",
        "Freezing rain",
    )

    private val weatherRisks = listOf(
        "Thunderstorm",
        "hail",
        "Blizzard",
        "Ice storm",
        "Extreme heat",
        "Flooding",
        NONE,
        NONE,
        NONE,
        NONE,
        NONE,
        NONE,
        NONE,
        NONE
    )

    private suspend fun generateMockDayWeather(lat: Double, long: Double): DayWeather {
        val currentDateTime = Calendar.getInstance()

        val (stateName: String, countryName: String) = getLocationName(lat, long)

        val currentWeather = generateCurrentWeather(currentDateTime, stateName, countryName)

        val weatherTimeLine = generateHourlyWeatherTimeline(currentDateTime)

        return DayWeather().apply {
            state = stateName
            country = countryName
            this.currentWeather = currentWeather
            timeLine = weatherTimeLine
        }
    }

    private fun generateMockWeatherTimeline(): List<TimelineWeather> {
        val currentDateTime = Calendar.getInstance()
        return generateHourlyWeatherTimeline(currentDateTime)
    }

    private fun generateHourlyWeatherTimeline(currentDateTime: Calendar): MutableList<TimelineWeather> {
        val currentHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val weatherTimeLine = mutableListOf<TimelineWeather>()
        for (hour in currentHour..currentDateTime.getActualMaximum(Calendar.HOUR_OF_DAY)) {
            val main = weatherConditions[(weatherConditions.indices).random()]
            val risk = weatherRisks[(weatherRisks.indices).random()]
            val timeInMillis = currentDateTime.timeInMillis
            weatherTimeLine.add(TimelineWeather(main, risk, timeInMillis))
            currentDateTime.roll(Calendar.HOUR_OF_DAY, true)
        }
        return weatherTimeLine
    }

    private fun generateDailyWeatherTimeline(currentDateTime: Calendar): MutableList<TimelineWeather> {
        val currentHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val weatherTimeLine = mutableListOf<TimelineWeather>()
        for (day in 1..7) {
            val main = weatherConditions[(weatherConditions.indices).random()]
            val risk = weatherRisks[(weatherRisks.indices).random()]
            val timeInMillis = currentDateTime.timeInMillis
            weatherTimeLine.add(TimelineWeather(main, risk, timeInMillis))
            currentDateTime.roll(Calendar.DATE, true)
        }
        return weatherTimeLine
    }

    private fun generateCurrentWeather(currentDateTime: Calendar, stateName: String, countryName: String): WeatherCondition {
        val currentMain = weatherConditions[(weatherConditions.indices).random()]
        val currentRisk = weatherRisks[(weatherRisks.indices).random()]
        val currentTime = currentDateTime.timeInMillis
        val currentTimeEnd = currentTime + (60 * 60 * 1000)
        return WeatherCondition(stateName, countryName, currentMain, currentRisk, currentTime, currentTimeEnd)
    }

    private suspend fun getLocationName(
        lat: Double,
        long: Double
    ): Pair<String, String> {
        var stateName: String
        var countryName: String
        getStateAndCountryName(lat, long).also {
            stateName = it.first
            countryName = it.second
        }
        return Pair(stateName, countryName)
    }

    private suspend fun getStateAndCountryName(lat: Double, long: Double): Pair<String, String> {
        val jsonResponse = GeoCodingNetworkApi.geoCodingRetrofitService.getLocationName(lat, long)
        val jsonObject = JSONObject(jsonResponse)
        return parseJsonToLocationName(jsonObject)
    }

    private fun parseJsonToLocationName(jsonObject: JSONObject): Pair<String, String> {
        val payLoadJsonObject = jsonObject.getJSONArray("geonames").getJSONObject(0)
        val stateName = payLoadJsonObject.getString("adminName1")
        val countryName = payLoadJsonObject.getString("countryName")
        return Pair(stateName, countryName)
    }

    private suspend fun getWeatherFromNetwork(lat: Double, long: Double): DayWeather {
        val jsonStringResponse = NetworkApi.retrofitService.getWeatherToday(lat, long)
        val jsonObject = JSONObject(jsonStringResponse)
        return parseJsonToDayWeather(jsonObject)
    }

    private suspend fun getWeatherFromNetworkForTomorrow(lat: Double, long: Double): List<TimelineWeather> {
        val jsonStringResponse = NetworkApi.retrofitService.getWeatherTomorrow(lat, long)
        val jsonArray = JSONArray(jsonStringResponse)
        return parseJsonToWeatherTimeline(jsonArray)
    }

    private suspend fun getWeatherFromNetworkForThisWeek(lat: Double, long: Double): List<TimelineWeather> {
        val jsonStringResponse = NetworkApi.retrofitService.getWeatherThisWeek(lat, long)
        val jsonArray = JSONArray(jsonStringResponse)
        return parseJsonToWeatherTimeline(jsonArray)
    }

    private fun parseJsonToDayWeather(jsonObject: JSONObject): DayWeather {
        val dayWeather = DayWeather()
        val state = jsonObject.getString("city")
        val country = jsonObject.getString("country")

        val currentInfo = jsonObject.getJSONObject("current")
        val currentMain = currentInfo.getString("main")
        val currentDateTime = getTimeInMillisFromString(currentInfo.getString("datetime"))
        val currentEndDateTime = getTimeInMillisFromString(currentInfo.getString("end_datetime"))
        val currentRisk = currentInfo.getString("risk")

        val timelineArray = jsonObject.getJSONArray("today_timeline")
        val hourWeathersList = mutableListOf<TimelineWeather>()
        for (index in 0 until timelineArray.length()) {
            val hourWeatherJSONObject = timelineArray.getJSONObject(index)
            val main = hourWeatherJSONObject.getString("main")
            val dateTime = getTimeInMillisFromString(hourWeatherJSONObject.getString("datetime"))
            val risk = hourWeatherJSONObject.getString("risk")
            hourWeathersList.add(TimelineWeather(main, risk, dateTime))
        }

        dayWeather.apply {
            this.state = state
            this.country = country
            currentWeather =
                WeatherCondition(state, country, currentMain, currentRisk, currentDateTime, currentEndDateTime)
            timeLine = hourWeathersList
        }

        return dayWeather
    }

    private fun parseJsonToWeatherTimeline(jsonArray: JSONArray): List<TimelineWeather> {

        val timelineWeathersList = mutableListOf<TimelineWeather>()
        for (index in 0 until jsonArray.length()) {
            val timelineWeatherJSONObject = jsonArray.getJSONObject(index)
            val main = timelineWeatherJSONObject.getString("main")
            val dateTime = getTimeInMillisFromString(timelineWeatherJSONObject.getString("datetime"))
            val risk = timelineWeatherJSONObject.getString("risk")
            timelineWeathersList.add(TimelineWeather(main, risk, dateTime))
        }

        return timelineWeathersList
    }

    private fun getTimeInMillisFromString(dateText: String): Long {
        val date = SimpleDateFormat.getDateTimeInstance().parse(dateText, ParsePosition(0))
        return date.time
    }
}