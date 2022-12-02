package com.gear.weathery.dashboard.repository

import android.util.Log
import com.gear.weathery.common.utils.Resource
import com.gear.weathery.dashboard.models.DayWeather
import com.gear.weathery.dashboard.models.HourWeather
import com.gear.weathery.dashboard.models.LinkResponse
import com.gear.weathery.dashboard.models.WeatherCondition
import com.gear.weathery.dashboard.network.GeoCodingNetworkApi
import com.gear.weathery.dashboard.network.NetworkApi
import com.gear.weathery.dashboard.network.ShareLinkNetworkApi
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
        NONE
    )

    private suspend fun generateMockDayWeather(lat: Double, long: Double): DayWeather {
        val currentDateTime = Calendar.getInstance()

        var stateName: String
        var countryName: String
        getStateAndCountryName(false,lat, long).also {
            stateName = it.first
            countryName = it.second
        }

        val currentMain = weatherConditions[(weatherConditions.indices).random()]
        val currentRisk = weatherRisks[(weatherRisks.indices).random()]
        val currentTime = currentDateTime.timeInMillis
        val currentTimeEnd = currentTime + (60 * 60 * 1000)
        val currentWeather = WeatherCondition(currentMain, currentRisk, currentTime, currentTimeEnd)

        val currentHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val weatherTimeLine = mutableListOf<HourWeather>()
        for (hour in currentHour..currentDateTime.getActualMaximum(Calendar.HOUR_OF_DAY)) {
            val main = weatherConditions[(weatherConditions.indices).random()]
            val risk = weatherRisks[(weatherRisks.indices).random()]
            val timeInMillis = currentDateTime.timeInMillis
            weatherTimeLine.add(HourWeather(main, risk, timeInMillis))
            currentDateTime.roll(Calendar.HOUR_OF_DAY, true)
        }

        return DayWeather().apply {
            state = stateName
            country = countryName
            this.currentWeather = currentWeather
            timeLine = weatherTimeLine
        }
    }

    private suspend fun getStateAndCountryName(fullDetails:Boolean,lat: Double, long: Double): Pair<String, String> {
        val jsonResponse = GeoCodingNetworkApi.geoCodingRetrofitService.getLocationName(lat, long)
        val jsonObject = JSONObject(jsonResponse)
        return if (!fullDetails) parseJsonToLocationName(jsonObject) else parseJsonToFullLocationName(jsonObject)
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
        return parseJsonToTodayWeather(jsonObject)
    }

    private fun parseJsonToTodayWeather(jsonObject: JSONObject): DayWeather {
        val dayWeather = DayWeather()
        val state = jsonObject.getString("city")
        val country = jsonObject.getString("country")

        val currentInfo = jsonObject.getJSONObject("current")
        val currentMain = currentInfo.getString("main")
        val currentDateTime = getTimeInMillisFromString(currentInfo.getString("datetime"))
        val currentEndDateTime = getTimeInMillisFromString(currentInfo.getString("end_datetime"))
        val currentRisk = currentInfo.getString("risk")

        val timelineArray = jsonObject.getJSONArray("today_timeline")
        val hourWeathersList = mutableListOf<HourWeather>()
        for (index in 0 until timelineArray.length()) {
            val hourWeatherJSONObject = timelineArray.getJSONObject(index)
            val main = hourWeatherJSONObject.getString("main")
            val dateTime = getTimeInMillisFromString(hourWeatherJSONObject.getString("datetime"))
            val risk = hourWeatherJSONObject.getString("risk")
            hourWeathersList.add(HourWeather(main, risk, dateTime))
        }

        dayWeather.apply {
            this.state = state
            this.country = country
            currentWeather =
                WeatherCondition(currentMain, currentRisk, currentDateTime, currentEndDateTime)
            timeLine = hourWeathersList
        }

        return dayWeather
    }

    private fun getTimeInMillisFromString(dateText: String): Long {
        val date = SimpleDateFormat.getDateTimeInstance().parse(dateText, ParsePosition(0))
        return date.time
    }

   suspend fun getSharedWeatherLink(lat:Double,lon:Double):Resource<LinkResponse>{
        var stateName: String
        var areaName: String
        getStateAndCountryName(true,lat, lon).also {
            stateName = it.first
            areaName = it.second
        }

       return try {
           val response = ShareLinkNetworkApi.shareLinkRetrofit.getShareLinkResponse(areaName,stateName,"Nigeria")
           if (!response.isSuccessful){
               Resource.Error(message = response.message())
           }else{
               Resource.Success(data = response.body())
           }

       }catch (e:Exception){
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