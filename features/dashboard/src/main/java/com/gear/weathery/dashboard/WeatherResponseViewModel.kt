package com.gear.weathery.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gear.weathery.dashboard.models.WeatherResponse
import com.gear.weathery.dashboard.repository.WeatherResponseRepository

class WeatherResponseViewModel: ViewModel {
    private var weatherRepository: WeatherResponseRepository? = null

    constructor() {
        weatherRepository =WeatherResponseRepository.getInstance()
    }

    fun setWeatherInfo(lat: Int, lon:Int) {
        weatherRepository!!.setWeatherApi(lat,lon)
    }

    fun getAllWeatherInfo(): LiveData<List<WeatherResponse>?> {
        return weatherRepository!!.getWeatherInfoFromApi()
    }
}