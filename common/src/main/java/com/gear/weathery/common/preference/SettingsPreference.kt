package com.gear.weathery.common.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
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

    fun darkMode(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[themeKey]
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
            preferences[vibrateModeKey]?:false
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

    suspend fun toggleVibrationMode(isDefault:Boolean){
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
}