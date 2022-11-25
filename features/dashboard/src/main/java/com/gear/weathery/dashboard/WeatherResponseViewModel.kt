package com.gear.weathery.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gear.weathery.dashboard.models.WeatherResponse
import com.gear.weathery.dashboard.repository.WeatherResponseRepository

class WeatherResponseViewModel(private val weatherRepo: WeatherResponseRepository) : ViewModel() {
  val allWeatherinfo : LiveData<List<WeatherResponse>?> = weatherRepo.getWeatherInfoFromApi()

//    init {
//        weatherRepository =WeatherResponseRepository.getInstance()
//    }

    fun setWeatherInfo(lat: Int, lon:Int) {
        weatherRepo.setWeatherApi(lat,lon)
    }

    fun getAllWeatherInfo(): LiveData<List<WeatherResponse>?> {
        return weatherRepo.getWeatherInfoFromApi()
    }
}