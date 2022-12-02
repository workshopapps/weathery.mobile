package com.gear.weathery.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gear.weathery.common.preference.SettingsPreference
import com.gear.weathery.setting.unitSettings.Units
import com.gear.weathery.setting.unitSettings.repo.UnitsImplRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(val settingsPreference: SettingsPreference, val UnitsImplRepo: UnitsImplRepo):ViewModel() {
    fun toggleDayMode(theme:String){
       viewModelScope.launch {
           settingsPreference.toggleDayMode(theme)
       }
    }

    fun togglePushNotification(isEnabled:Boolean){
        viewModelScope.launch {
            settingsPreference.togglePushNotification(isEnabled)
        }
    }

    fun setPushNotification(frequency:String){
        viewModelScope.launch {
            settingsPreference.setPushNotification(frequency)
        }
    }

    fun setNotificationTone(toneName:String?){
        viewModelScope.launch {
            settingsPreference.setNotificationTone(toneName)
        }
    }

    fun toggleVibrationMode(isDefault:Boolean){
        viewModelScope.launch {
            settingsPreference.toggleVibrationMode(isDefault)
        }
    }

    fun toggleBanner(isEnabled:Boolean){
        viewModelScope.launch {
            settingsPreference.toggleBanner(isEnabled)
        }
    }

    fun saveLanguage(lang:String){
        viewModelScope.launch {
            settingsPreference.saveLanguage(lang)
        }
    }

    var temperature : MutableLiveData<String> = MutableLiveData("")
    var pressure : MutableLiveData<String> = MutableLiveData("")
    var windSpeed : MutableLiveData<String> = MutableLiveData("")

    var units : MutableLiveData<Units> = MutableLiveData()

    fun saveUnits() {
        viewModelScope.launch(Dispatchers.IO) {
            UnitsImplRepo.saveUnits(
                Units(
                    temperature = temperature.value!!,
                    pressure = pressure.value!!,
                    windSpeed = windSpeed.value!!
                )
            )
        }
    }

    fun getUnits() {
        viewModelScope.launch(Dispatchers.IO) {
            UnitsImplRepo.getUnits().collect {
                units.postValue(it)
            }
        }
    }

}