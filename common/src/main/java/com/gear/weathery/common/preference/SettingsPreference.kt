package com.gear.weathery.common.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsPreference @Inject constructor(private val dataStore: DataStore<Preferences>) {
    private val themeKey = stringPreferencesKey("theme")
    private val pushNotificationKey = booleanPreferencesKey("push_notification")
    private val notificationFrequencyKey = stringPreferencesKey("push_notification_frequency")
    private val bannerKey = booleanPreferencesKey("banner")
    private val vibrateModeKey = booleanPreferencesKey("vibrate_key")
    private val toneKey = stringPreferencesKey("tone_key")
    private val langKey = stringPreferencesKey("lang_key")
    private val appForegroundStatusKey = booleanPreferencesKey("app_foreground")
    private val unreadNotificationCounterKey = intPreferencesKey("unread_notifications_counter")


    fun darkMode(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[themeKey]?:"system"
        }
    }
    fun pushNotification(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[pushNotificationKey]?:false
        }
    }

    fun pushNotificationFrequency(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[notificationFrequencyKey]?:""
        }
    }

    fun notificationTone () = dataStore.data.map { preferences ->
        preferences[toneKey] }

    fun currentLanguage() = dataStore.data.map { preferences ->
        preferences[langKey]?:"en" }

    fun vibrateMode(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[vibrateModeKey]?:true
        }
    }

    fun bannerEnabled(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[bannerKey]?:false
        }
    }

    suspend fun toggleDayMode(theme:String){
        dataStore.edit { preference ->
            preference[themeKey] = theme

        }
    }

    suspend fun togglePushNotification(isEnabled:Boolean){
        dataStore.edit { preference ->
            preference[pushNotificationKey] = isEnabled

        }
    }

    suspend fun setPushNotification(frequency:String){
        dataStore.edit { preference ->
            preference[notificationFrequencyKey] = frequency

        }
    }

    suspend fun setNotificationTone(toneName:String?){
        dataStore.edit { preference ->
            preference[toneKey] = toneName?:""
        }
    }

    suspend fun toggleVibrationMode(isDefault:Boolean=true){
        dataStore.edit { preference ->
            preference[vibrateModeKey] = isDefault
        }
    }

    suspend fun toggleBanner(isEnabled:Boolean){
        dataStore.edit { preference ->
            preference[bannerKey] = isEnabled

        }
    }

    suspend fun saveLanguage(lang:String){
        dataStore.edit { preference ->
            preference[langKey] = lang

        }
    }

    suspend fun updateAppForegroundStatus(newStatus:Boolean){
        dataStore.edit { preference ->
            preference[appForegroundStatusKey] = newStatus

        }
    }

    suspend fun getAppForegroundStatus():Boolean{
        return dataStore.data.map {
            it[appForegroundStatusKey] ?: true
        }.first()
    }

    suspend fun updateUnreadNotificationCounter(newValue: Int){
        dataStore.edit { preference ->
            preference[unreadNotificationCounterKey] = newValue

        }
    }

    fun unreadNotificationCounterFlow():Flow<Int>{
        return dataStore.data.map {
            it[unreadNotificationCounterKey] ?: 0
        }
    }
}