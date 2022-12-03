package com.gear.weathery.dashboard.repository

import androidx.lifecycle.LiveData
import com.gear.weathery.dashboard.models.WeatherResponse
import com.gear.weathery.dashboard.request.WeatherApiClient

class WeatherResponseRepository {

    private var weatherApiClient = WeatherApiClient.getInstance()
    private var lon: Int? = null
    private var lat: Int? = null

    companion object{
        private var instance: WeatherResponseRepository? = null
        fun getInstance() : WeatherResponseRepository {
            if (instance == null){
                instance = WeatherResponseRepository()
            }
            return instance as WeatherResponseRepository
        }
    }

    fun setWeatherApi(lat: Int, lon: Int) {
        this.lon = lon
        this.lat = lat
       WeatherApiClient.getInstance().getWeatherApi(lat, lon)
    }

    fun getWeatherInfoFromApi(): LiveData<List<WeatherResponse>?> {
        return weatherApiClient.getWeatherInfo()
    }


}