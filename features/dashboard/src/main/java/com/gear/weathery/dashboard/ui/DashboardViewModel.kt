package com.gear.weathery.dashboard.ui

import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.gear.weathery.common.utils.Resource
import com.gear.weathery.dashboard.models.DayWeather
import com.gear.weathery.dashboard.models.LinkResponse
import com.gear.weathery.dashboard.models.TimelineWeather
import com.gear.weathery.dashboard.models.WeatherCondition
import com.gear.weathery.dashboard.repository.WeatherRepo
import com.gear.weathery.dashboard.repository.WeatherResponseRepository
import com.gear.weathery.location.api.LocationsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

const val TODAY_VIEW_MODE = "today view mode"
const val TOMORROW_VIEW_MODE = "tomorrow view mode"
const val THIS_WEEK_VIEW_MODE = "this week view mode"

const val DEFAULT = 0
const val BUSY = 1
const val PASSED = 2
const val FAILED = 3

class DashboardViewModel(private val locationRepository: LocationsRepository): ViewModel() {

    private var _dayWeather = MutableLiveData<DayWeather>()
    val dayWeather: LiveData<DayWeather> get() = _dayWeather

    private val _sharedLinkEvent = Channel<ShareLinkEvents>()
    val sharedLinkEvent = _sharedLinkEvent.receiveAsFlow()

    sealed class ShareLinkEvents {
        class Successful(val data:Resource<LinkResponse>) : ShareLinkEvents()
        object Failure : ShareLinkEvents()
    }


    private var _currentWeather = MutableLiveData<WeatherCondition>()
    val currentWeather: LiveData<WeatherCondition> get() = _currentWeather

    private var _timeline = MutableLiveData<Pair<List<TimelineWeather>, String>>()
    val timeline: LiveData<Pair<List<TimelineWeather>, String>> get() = _timeline

    private var _viewMode = MutableLiveData<String>()
    val viewMode: LiveData<String> get() = _viewMode

    private lateinit var latestLocation: Location

    private var _currentWeatherStatus = MutableLiveData<Int>()
    val currentWeatherStatus: LiveData<Int> get() = _currentWeatherStatus

    private var _timelineStatus = MutableLiveData<Int>()
    val timelineStatus: LiveData<Int> get() = _timelineStatus


    fun updateCurrentLocation(location: Location?) {
        if (location == null){
            return
        }

        latestLocation = location
        updateWeatherWithLocation(location.latitude, location.longitude)
    }

    private fun updateWeatherWithLocation(lat: Double = latestLocation.latitude, long: Double = latestLocation.longitude) {

        viewModelScope.launch {
            _currentWeatherStatus.value = BUSY
            _timelineStatus.value = BUSY

            val dayWeather = WeatherRepo.getWeatherForToday(lat, long)
            _currentWeather.value = dayWeather.currentWeather
            _timeline.value = Pair(dayWeather.timeLine, HOURLY_TIMELINE)

            _currentWeatherStatus.value = PASSED
            _timelineStatus.value = PASSED

            _viewMode.value = TODAY_VIEW_MODE
        }
    }

    fun showTomorrowView(){
        _viewMode.value = TOMORROW_VIEW_MODE
        showTomorrowWeather()
    }

    fun showTodayView(){
        _viewMode.value = TODAY_VIEW_MODE
        updateWeatherWithLocation()
    }

    fun showThisWeekView(){
        _viewMode.value = THIS_WEEK_VIEW_MODE
        showThisWeekWeather()
    }

    private fun showTomorrowWeather(){
        viewModelScope.launch {
            _timelineStatus.value = BUSY

            val tomorrowWeatherTimeline = WeatherRepo.getTomorrowWeatherTimeline(latestLocation.latitude, latestLocation.longitude)
            _timeline.value = Pair(tomorrowWeatherTimeline, HOURLY_TIMELINE)

            _timelineStatus.value = PASSED
        }
    }

    private fun showThisWeekWeather(){
        viewModelScope.launch {
            _timelineStatus.value = BUSY

            val thisWeekWeatherTimeline = WeatherRepo.getThisWeekTimeline(latestLocation.latitude, latestLocation.longitude)
            _timeline.value = Pair(thisWeekWeatherTimeline, DAILY_TIMELINE)

            _timelineStatus.value = PASSED
        }
    }

    init {
        _viewMode.value = TODAY_VIEW_MODE
        _currentWeatherStatus.value = BUSY
        _timelineStatus.value = BUSY
    }




    fun getSharedWeatherLink(lat: Double,long: Double){
        viewModelScope.launch {
            _sharedLinkEvent.send(ShareLinkEvents.Successful(WeatherRepo.getSharedWeatherLink(lat,long)))
        }
    }

    val locationFlow = MutableStateFlow<List<com.gear.weathery.location.api.Location>>(emptyList())
    fun getSavedLocation(){
        viewModelScope.launch {
            locationRepository.locations.collectLatest {
                locationFlow.value = it
            }
        }
    }

    fun updateSavedWeatherView(lat: Double, long: Double){
        updateWeatherWithLocation(lat=lat, long =  long)
    }

    class DashboardViewModelFactory(private val locationRepository: LocationsRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DashboardViewModel(locationRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}