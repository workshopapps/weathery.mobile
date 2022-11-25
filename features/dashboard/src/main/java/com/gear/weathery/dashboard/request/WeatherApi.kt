package com.gear.weathery.dashboard.request

import com.gear.weathery.dashboard.models.TimeWeather
import com.gear.weathery.dashboard.models.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather/forecasts")
    fun getWeatherInfo(
        @Query("lat") latitude:Int,
        @Query("lon") longitude : Int

    ): Call<List<TimeWeather>>
}