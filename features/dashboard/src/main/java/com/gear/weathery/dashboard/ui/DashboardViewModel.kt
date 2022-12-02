package com.gear.weathery.dashboard.ui

import android.location.Location
import androidx.lifecycle.*
import com.gear.weathery.common.utils.Resource
import com.gear.weathery.dashboard.models.DayWeather
import com.gear.weathery.dashboard.models.LinkResponse
import com.gear.weathery.dashboard.repository.WeatherRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DashboardViewModel: ViewModel() {

    private var _dayWeather = MutableLiveData<DayWeather>()
    val dayWeather: LiveData<DayWeather> get() = _dayWeather

    private val _sharedLinkEvent = Channel<ShareLinkEvents>()
    val sharedLinkEvent = _sharedLinkEvent.receiveAsFlow()

    sealed class ShareLinkEvents {
        class Successful(val data:Resource<LinkResponse>) : ShareLinkEvents()
        object Failure : ShareLinkEvents()
    }



    fun updateWeatherWithNewLocation(location: Location?) {
        if (location == null){
            return
        }

        viewModelScope.launch {
            val dayWeather = WeatherRepo.getWeatherForToday(location.latitude, location.longitude)
            _dayWeather.value = dayWeather
        }
    }

    fun updateWeatherWithNewLocation(lat: Double, long: Double) {
        viewModelScope.launch {
            val dayWeather = WeatherRepo.getWeatherForToday(lat, long)
            _dayWeather.value = dayWeather
        }
    }

    fun getSharedWeatherLink(lat: Double,long: Double){
        viewModelScope.launch {
            _sharedLinkEvent.send(ShareLinkEvents.Successful(WeatherRepo.getSharedWeatherLink(lat,long)))
        }
    }

    class DashboardViewModelFactory() :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DashboardViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}