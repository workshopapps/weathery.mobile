package com.gear.weathery.setting.notifications.database

import androidx.room.ColumnInfo
import androidx.room.Entity


data class DBNotification(
    @ColumnInfo()
    val message: String, val time: String, val event: String
)