package com.gear.weathery.dashboard.ui

import android.location.Location
import androidx.lifecycle.*
import com.gear.weathery.common.utils.Resource
import com.gear.weathery.dashboard.models.*
import com.gear.weathery.dashboard.repository.WeatherRepo
import com.gear.weathery.location.api.LocationsRepository
import com.gear.weathery.setting.notifications.database.NotificationDao
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

class DashboardViewModel(private val locationRepository: LocationsRepository, private val notificationDao: NotificationDao): ViewModel() {

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

    private lateinit var deviceLocation: Location
    private lateinit var selectedLocation: Pair<Double, Double>

    private var _currentWeatherStatus = MutableLiveData<Int>()
    val currentWeatherStatus: LiveData<Int> get() = _currentWeatherStatus

    private var _timelineStatus = MutableLiveData<Int>()
    val timelineStatus: LiveData<Int> get() = _timelineStatus

    val notificationsNumber = Transformations.map(notificationDao.getNotifications().asLiveData()){
        it.size
    }

    private var _locationFlow = MutableStateFlow<List<com.gear.weathery.location.api.Location>>(emptyList())
    val locationFlow = _locationFlow.asStateFlow()

    fun updateDeviceLocation(location: Location?) {
        if (location == null){
            return
        }

        deviceLocation = location
        selectedLocation = Pair(location.latitude, location.longitude)
        updateWeatherWithLocation(location.latitude, location.longitude)
    }

    fun getLatLongDeviceLocation(): Pair<Double, Double>{
        return Pair(deviceLocation.latitude, deviceLocation.longitude)
    }

    fun getDeviceLocation(): Location{
        return deviceLocation
    }

    fun getLatLongSelectedLocation(): Pair<Double, Double>{
        return selectedLocation
    }


    private fun updateWeatherWithLocation(lat: Double = selectedLocation.first, long: Double = selectedLocation.second){

        viewModelScope.launch {
            _currentWeatherStatus.value = BUSY
            _timelineStatus.value = BUSY

            when(val dayWeatherResponse = WeatherRepo.getWeatherForToday(lat, long)) {

                is DayWeatherResponse.SuccessDayWeatherResponse -> {
                    _currentWeather.value = dayWeatherResponse.payLoad!!.currentWeather
                    _timeline.value = Pair(dayWeatherResponse.payLoad.timeLine, HOURLY_TIMELINE)
                    _currentWeatherStatus.value = PASSED
                    _timelineStatus.value = PASSED
                }

                is DayWeatherResponse.FailureDayWeatherResponse -> {
                    _currentWeatherStatus.value = FAILED
                    _timelineStatus.value = FAILED
                }
            }

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

            when(val tomorrowTimelineResponse = WeatherRepo.getTomorrowWeatherTimeline(selectedLocation.first, selectedLocation.second)){
                is TimelineResponse.SuccessTimelineResponse -> {
                    _timeline.value = Pair(tomorrowTimelineResponse.payLoad!!, HOURLY_TIMELINE)
                    _timelineStatus.value = PASSED
                }
                else -> {
                    _timelineStatus.value = FAILED
                    _currentWeatherStatus.value = FAILED
                }
            }
        }
    }

    private fun showThisWeekWeather(){
        viewModelScope.launch {
            _timelineStatus.value = BUSY

            when(val thisWeekTimelineResponse = WeatherRepo.getThisWeekTimeline(selectedLocation.first, selectedLocation.second)){
                is TimelineResponse.SuccessTimelineResponse -> {
                    _timeline.value = Pair(thisWeekTimelineResponse.payLoad!!, DAILY_TIMELINE)
                    _timelineStatus.value = PASSED
                }
                else -> {
                    _timelineStatus.value = FAILED
                    _currentWeatherStatus.value = FAILED
                }
            }
        }
    }

    init {
        _viewMode.value = TODAY_VIEW_MODE
        _currentWeatherStatus.value = DEFAULT
        _timelineStatus.value = DEFAULT
        getSavedLocation()
    }


    fun getSharedWeatherLink(lat: Double = deviceLocation.latitude,long: Double = deviceLocation.longitude){
        viewModelScope.launch {
            _sharedLinkEvent.send(ShareLinkEvents.Successful(WeatherRepo.getSharedWeatherLink(lat,long)))
        }
    }


 private fun getSavedLocation(){
        viewModelScope.launch {
            locationRepository.locations.collectLatest {
                _locationFlow.value = it
            }
        }
    }

    fun updateSelectedLocation(lat: Double, long: Double){
        selectedLocation = Pair(lat, long)
        updateWeatherWithLocation()
    }

    fun setDefaultMode(){
        _currentWeatherStatus.value = DEFAULT
        _timelineStatus.value = DEFAULT
    }

    class DashboardViewModelFactory(private val locationRepository: LocationsRepository, private val notificationDao: NotificationDao) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DashboardViewModel(locationRepository, notificationDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}