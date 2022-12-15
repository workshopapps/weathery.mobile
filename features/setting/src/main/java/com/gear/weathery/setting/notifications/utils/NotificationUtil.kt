package com.gear.weathery.setting.notifications.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.gear.weathery.common.preference.SettingsPreference
import com.gear.weathery.setting.R
import com.gear.weathery.setting.notifications.database.NotificationDao
import com.gear.weathery.setting.notifications.model.NotificationData
import com.gear.weathery.setting.util.Constants.CHANNEL_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

const val NOTIFICATION_ID = 3
fun NotificationManager.sendNotification(context: Context, event: String, message: String) {

    val pendingIntent = NavDeepLinkBuilder(context)
        .setGraph(R.navigation.notifications_nav_graph)
        .setDestination(R.id.notifications)
        .createPendingIntent()

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle(event)
        .setContentText(message)
        .setSmallIcon(R.drawable.ic_app_tropical)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}

fun processNotificationData(
    event: String,
    body: String,
    dateTime: String,
    notificationDao: NotificationDao,
    context: Context,
    settingsPreference: SettingsPreference
) {
    val scope = CoroutineScope(Job() + Dispatchers.Main)

    scope.launch {

        incrementUnreadNotificationCounter(settingsPreference)

        val notificationData =
            NotificationData(event, body, dateTime)
        notificationDao.insert(notificationData)

        if (!settingsPreference.getAppForegroundStatus()){
            (ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager).sendNotification(context, event, body)
        }

    }
}

suspend fun incrementUnreadNotificationCounter(settingsPreference: SettingsPreference) {
    var unreadNotificationCounter = settingsPreference.unreadNotificationCounterFlow().first()
    unreadNotificationCounter++
    settingsPreference.updateUnreadNotificationCounter(unreadNotificationCounter)
}
