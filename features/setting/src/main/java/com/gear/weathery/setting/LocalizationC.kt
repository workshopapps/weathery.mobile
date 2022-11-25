package com.gear.weathery.setting

import android.app.Application
import com.yariksoffice.lingver.Lingver

class LocalizationC : Application() {

    override fun onCreate() {
        super.onCreate()
        Lingver.init(this)
    }
}