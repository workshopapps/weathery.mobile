package com.gear.weathery.setting.notifications.utils


import java.text.ParsePosition
import java.text.SimpleDateFormat

const val EXTENDED_RESPONSE_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss"

fun getTimeInMillisFromString(
    dateText: String,
    pattern: String = EXTENDED_RESPONSE_DATE_TIME_PATTERN
): Long {
    val parsePos = ParsePosition(0)
    val date = SimpleDateFormat(pattern).parse(dateText, parsePos)
    return date.time
}