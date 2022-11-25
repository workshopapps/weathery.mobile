package com.gear.weathery.dashboard.ui

import android.location.Location
import androidx.lifecycle.*
import com.gear.weathery.dashboard.models.TimeWeather
import com.gear.weathery.dashboard.models.UITimeWeather
import com.gear.weathery.dashboard.network.MockNetworkService
import com.gear.weathery.dashboard.network.NetworkApi
import com.gear.weathery.dashboard.util.toTimeWeathers
import com.gear.weathery.dashboard.util.toUITimeWeathers
import kotlinx.coroutines.launch

class DashBoardViewModel(): ViewModel() {

    private var _timeWeathers = MutableLiveData<List<UITimeWeather>>()
    val timeWeather: LiveData<List<UITimeWeather>> get() = _timeWeathers




    fun updateLocation(location: Location){
        viewModelScope.launch {
//            // correct network call for when backend api is available
//            val weathersFromNetwork = NetworkApi.retrofitService.getWeathers(location.latitude.toInt(), location.longitude.toInt())
//            _timeWeathers.value = weathersFromNetwork.toTimeWeathers().toUITimeWeathers()

        }
    }

    fun updateHighLightedTimeWeather(id: Int){
        val uiTimeWeathers = _timeWeathers.value!!.map {
            it.highlighted = false
            it
        }
        uiTimeWeathers.find { it.id == id }?.highlighted = true
        _timeWeathers.value = uiTimeWeathers
    }

    fun generateShareWeatherText(): String{
        var text = "Today's Weather timeline: \n\n"
        for(weather in _timeWeathers.value!!){
            text += "${weather.time}: ${weather.main}, ${weather.description}\n"
        }
        text += "\n\npowered by Weathery"
        return text
    }


    init {
        // mock network call
        val weathersFromNetwork = MockNetworkService.getWeathers()
        _timeWeathers.value = weathersFromNetwork.toTimeWeathers().toUITimeWeathers()
        updateHighLightedTimeWeather(0)
    }
}

class DashBoardViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashBoardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashBoardViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}