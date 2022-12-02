package com.gear.weathery.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gear.weathery.common.preference.SettingsPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(val settingsPreference: SettingsPreference):ViewModel() {
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

}