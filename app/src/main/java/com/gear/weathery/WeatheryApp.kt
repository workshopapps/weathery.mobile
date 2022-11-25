package com.gear.weathery

import android.app.Application
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatheryApp: Application () {

    override fun onCreate() {
        super.onCreate()
        Lingver.init(this)
    }

}