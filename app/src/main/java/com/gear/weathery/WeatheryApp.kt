package com.gear.weathery

import android.app.Application
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.gear.weathery.common.preference.SettingsPreference
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class WeatheryApp: Application () {

    val applicationScope = MainScope()
    @Inject
    lateinit var settingsPreference:SettingsPreference
    override fun onCreate() {
        super.onCreate()
        applicationScope.launch{
            settingsPreference.darkMode().collect{theme ->
                when (theme) {
                    "light" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    "dark" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                   }
                    "system" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                   }
                }
            }
        }
        Lingver.init(this)
    }

}