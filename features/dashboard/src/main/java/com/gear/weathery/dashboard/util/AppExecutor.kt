package com.gear.weathery.dashboard.util

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class AppExecutor {
    companion object{
        private var instance: AppExecutor? = null
        fun getInstance(): AppExecutor {
            if(instance == null){
                instance = AppExecutor()
            }
            return instance as AppExecutor
        }
    }

    private val mNetworkIO = Executors.newScheduledThreadPool(3)

    fun networkIO(): ScheduledExecutorService? {
        return mNetworkIO
    }
}