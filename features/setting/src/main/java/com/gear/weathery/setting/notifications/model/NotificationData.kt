package com.gear.weathery.setting.notifications.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationData(@PrimaryKey(autoGenerate = true) val id: Int = 0,
                            val notificationText:String, val notificationTime : String,
                            val notificationEvent:String)