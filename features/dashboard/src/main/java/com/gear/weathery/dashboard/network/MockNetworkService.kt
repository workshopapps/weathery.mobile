package com.gear.weathery.dashboard.network

import com.gear.weathery.dashboard.models.NWTimeWeather

object MockNetworkService {

    private val timesList = listOf("12AM" , "3AM", "6AM", "9AM", "12PM", "3PM", "6PM", "9PM")
    private val mainsAndDescriptions = mutableListOf<Pair<String, String>>()
    private const val dummyDate = "21st Oct"


    fun getWeathers(): List<NWTimeWeather>{
        val weathersList = mutableListOf<NWTimeWeather>()
        for (time in timesList){
            val randomMainAndDescription = mainsAndDescriptions[(0 until mainsAndDescriptions.size).random()]
            weathersList.add(NWTimeWeather(randomMainAndDescription.first, randomMainAndDescription.second, dummyDate, time))
        }
        return weathersList
    }

    init {
        mainsAndDescriptions.add(Pair("Clear", "clear sky"))
        mainsAndDescriptions.add(Pair("Clouds", "scattered clouds"))
        mainsAndDescriptions.add(Pair("Clouds", "broken clouds"))
        mainsAndDescriptions.add(Pair("Clouds", "overcast clouds"))
        mainsAndDescriptions.add(Pair("Rain", "heavy rain"))
        mainsAndDescriptions.add(Pair("Rain", "light shower"))
        mainsAndDescriptions.add(Pair("Rain", "drizzle"))
    }
}