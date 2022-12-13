package com.gear.weathery.setting.notifications.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gear.weathery.setting.notifications.utils.getTimeInMillisFromString
import java.text.ParsePosition
import java.text.SimpleDateFormat


@Entity(tableName = "notifications")
data class NotificationData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val notificationText: String, val notificationTime: Long,
    val notificationEvent: String
) {

    constructor(event: String, message: String, dateTime: String) : this(
        notificationEvent = event,
        notificationText = message,
        notificationTime = getTimeInMillisFromString(dateTime)
    )

}