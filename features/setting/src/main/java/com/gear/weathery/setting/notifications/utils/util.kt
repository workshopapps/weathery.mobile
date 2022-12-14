package com.gear.weathery.setting.notifications.utils

import java.text.ParsePosition
import java.text.SimpleDateFormat

const val NOTIFICATION_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm"

fun getTimeInMillisFromString(dateText: String, pattern: String = NOTIFICATION_DATE_TIME_PATTERN): Long {
    val parsePos = ParsePosition(0)
    val date = SimpleDateFormat(pattern).parse(dateText, parsePos)
    return date.time
}