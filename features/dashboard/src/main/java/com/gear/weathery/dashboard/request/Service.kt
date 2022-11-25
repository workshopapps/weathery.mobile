package com.gear.weathery.dashboard.request

import com.gear.weathery.dashboard.models.WeatherResponse
import com.gear.weathery.dashboard.util.Credentials
import com.gear.weathery.dashboard.util.Credentials.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Service {
    companion object{
        private val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        private val retrofit = retrofitBuilder.build()

        private val weatherApi: WeatherApi = retrofit.create(WeatherApi::class.java)

        fun getWeatherApi(): WeatherApi {
            return weatherApi
        }
    }
}