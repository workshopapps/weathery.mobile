package com.gear.weathery.dashboard.repository

import android.util.Log
import com.gear.weathery.common.utils.Resource
import com.gear.weathery.dashboard.models.*
import com.gear.weathery.dashboard.models.DayWeatherResponse.*
import com.gear.weathery.dashboard.network.GeoCodingNetworkApi
import com.gear.weathery.dashboard.network.NetworkApi
import org.json.JSONArray
import com.gear.weathery.dashboard.network.ShareLinkNetworkApi
import org.json.JSONObject
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Calendar

const val NONE = "null"
const val EXTENDED_RESPONSE_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm"
const val TIMELINE_RESPONSE_DATE_TIME_PATTERN = "d MMM, yyyy h:mma"
const val TIMELINE_RESPONSE_DATE_TIME_PATTERN_X = "yyyy-MM-dd'T'HH:mm:ss"

object WeatherRepo {

    suspend fun getWeatherForToday(lat: Double, long: Double): DayWeatherResponse {
        return try {
            SuccessDayWeatherResponse(getWeatherFromNetwork(lat, long))
        } catch (e: Exception) {
            FailureDayWeatherResponse()
        }
    }

    suspend fun getTomorrowWeatherTimeline(lat: Double, long: Double): TimelineResponse {

        return try {
            TimelineResponse.SuccessTimelineResponse(getWeatherFromNetworkForTomorrow(lat, long))
        } catch (_: Exception) {
            TimelineResponse.FailureTimelineResponse()
        }

    }

    suspend fun getThisWeekTimeline(lat: Double, long: Double): TimelineResponse {

        return try {
            TimelineResponse.SuccessTimelineResponse(getWeatherFromNetworkForThisWeek(lat, long))
        } catch (_: Exception) {
            TimelineResponse.FailureTimelineResponse()
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

    private fun generateCurrentWeather(
        currentDateTime: Calendar,
        stateName: String,
        countryName: String
    ): WeatherCondition {
        val currentMain = weatherConditions[(weatherConditions.indices).random()]
        val currentRisk = weatherRisks[(weatherRisks.indices).random()]
        val currentTime = currentDateTime.timeInMillis
        val currentTimeEnd = currentTime + (60 * 60 * 1000)
        return WeatherCondition(
            stateName,
            countryName,
            currentMain,
            currentRisk,
            currentTime,
            currentTimeEnd
        )
    }

    private suspend fun getLocationName(
        lat: Double,
        long: Double
    ): Pair<String, String> {
        var stateName: String
        var countryName: String
        getStateAndCountryName(false, lat, long).also {
            stateName = it.first
            countryName = it.second
        }
        return Pair(stateName, countryName)
    }

    private suspend fun getStateAndCountryName(
        fullDetails: Boolean,
        lat: Double,
        long: Double
    ): Pair<String, String> {
        val jsonResponse = GeoCodingNetworkApi.geoCodingRetrofitService.getLocationName(lat, long)
        val jsonObject = JSONObject(jsonResponse)
        return if (!fullDetails) parseJsonToLocationName(jsonObject) else parseJsonToFullLocationName(
            jsonObject
        )
    }

    private fun parseJsonToLocationName(jsonObject: JSONObject): Pair<String, String> {
        val payLoadJsonObject = jsonObject.getJSONArray("geonames").getJSONObject(0)
        val stateName = payLoadJsonObject.getString("adminName1")
        val countryName = payLoadJsonObject.getString("countryName")
        return Pair(stateName, countryName)
    }

    private suspend fun getWeatherFromNetwork(lat: Double, long: Double): DayWeather {
        var jsonStringResponse = ""
        jsonStringResponse = try {
            NetworkApi.retrofitService.getWeatherToday(lat, long)
        } catch (e: Exception) {
            Log.e("parsing", e.message ?: e.toString())
            throw e
        }

        val jsonObject = JSONObject(jsonStringResponse)
        return parseJsonToDayWeather(jsonObject)
    }

    private suspend fun getWeatherFromNetworkForTomorrow(
        lat: Double,
        long: Double
    ): List<TimelineWeather> {
        val jsonStringResponse = NetworkApi.retrofitService.getWeatherTomorrow(lat, long)
        val jsonArray = JSONArray(jsonStringResponse)
        return parseJsonToWeatherTimeline(jsonArray)
    }

    private suspend fun getWeatherFromNetworkForThisWeek(
        lat: Double,
        long: Double
    ): List<TimelineWeather> {
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
        val currentDateTime = getTimeInMillisFromString(
            currentInfo.getString("datetime"),
            EXTENDED_RESPONSE_DATE_TIME_PATTERN
        )
        val currentEndDateTime = getTimeInMillisFromString(
            currentInfo.getString("end_datetime"),
            EXTENDED_RESPONSE_DATE_TIME_PATTERN
        )
        val currentRisk = currentInfo.getString("risk")

        val timelineArray = jsonObject.getJSONArray("todays_timeline")
        val hourWeathersList = mutableListOf<TimelineWeather>()
        for (index in 0 until timelineArray.length()) {
            val hourWeatherJSONObject = timelineArray.getJSONObject(index)
            val main = hourWeatherJSONObject.getString("main")
            val dateTime = getTimeInMillisFromString(
                hourWeatherJSONObject.getString("datetime"),
                EXTENDED_RESPONSE_DATE_TIME_PATTERN
            )
            val risk = hourWeatherJSONObject.getString("risk")
            hourWeathersList.add(TimelineWeather(main, risk, dateTime))
        }

        dayWeather.apply {
            this.state = state
            this.country = country
            currentWeather =
                WeatherCondition(
                    state,
                    country,
                    currentMain,
                    currentRisk,
                    currentDateTime,
                    currentEndDateTime
                )
            timeLine = hourWeathersList
        }

        return dayWeather
    }

    private fun parseJsonToWeatherTimeline(jsonArray: JSONArray): List<TimelineWeather> {

        val timelineWeathersList = mutableListOf<TimelineWeather>()
        for (index in 0 until jsonArray.length()) {
            val timelineWeatherJSONObject = jsonArray.getJSONObject(index)
            val main = timelineWeatherJSONObject.getString("main")
//            val timeResponse = timelineWeatherJSONObject.getString("time")
//            val dateResponse = timelineWeatherJSONObject.getString("datetime")
            val dateTimeResponse = timelineWeatherJSONObject.getString("datetime")
            val dateTime =
                getTimeInMillisFromString(dateTimeResponse, TIMELINE_RESPONSE_DATE_TIME_PATTERN_X)
            val risk = timelineWeatherJSONObject.getString("risk")
            timelineWeathersList.add(TimelineWeather(main, risk, dateTime))
        }

        return timelineWeathersList
    }

    private fun getTimeInMillisFromString(dateText: String, pattern: String): Long {
        val parsePos = ParsePosition(0)
        val date = SimpleDateFormat(pattern).parse(dateText, parsePos)
        return date.time
    }

    suspend fun getSharedWeatherLink(lat: Double, lon: Double): Resource<LinkResponse> {
        var stateName: String
        var areaName: String
        getStateAndCountryName(true, lat, lon).also {
            stateName = it.first
            areaName = it.second
        }

        return try {
            val response = ShareLinkNetworkApi.shareLinkRetrofit.getShareLinkResponse(
                areaName,
                stateName,
                "Nigeria"
            )
            if (!response.isSuccessful) {
                Resource.Error(message = response.message())
            } else {
                Resource.Success(data = response.body())
            }

        } catch (e: Exception) {
            Resource.Error(message = e.toString())

        }
    }

    private fun parseJsonToFullLocationName(jsonObject: JSONObject): Pair<String, String> {
        val payLoadJsonObject = jsonObject.getJSONArray("geonames").getJSONObject(0)
        val stateName = payLoadJsonObject.getString("adminName1")
        val areaName = payLoadJsonObject.getString("toponymName")
        return Pair(stateName, areaName)
    }
}