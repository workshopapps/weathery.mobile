package com.gear.weathery

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.gear.weathery.common.preference.SettingsPreference
import com.gear.weathery.setting.R
import com.gear.weathery.setting.notifications.database.NotificationDao
import com.gear.weathery.setting.notifications.model.NotificationData
import com.gear.weathery.setting.util.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseNotificationService : FirebaseMessagingService(){
    @Inject
    lateinit var notificationDao: NotificationDao

    @Inject
    lateinit var settingsPreference: SettingsPreference
    override fun onMessageReceived(message: RemoteMessage) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            super.onMessageReceived(message)
            val random = message.data

            val event = random.get("event")
            val body = random.get("message")
            val time = random.get("datetime")

            val notification = NotificationData(notificationText = body!!, notificationTime = time!!, notificationEvent = event!!)
            notificationDao.insert(notification)


            val notificationBuilder = NotificationCompat.Builder(this@FirebaseNotificationService)
                .setContentTitle(event)
                .setContentText(body)
                .setSmallIcon(R.drawable.tropical_logo)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.rain))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager

            CoroutineScope(Dispatchers.Main).launch{
                settingsPreference.pushNotification().collect{
                    if (!it){
                        notificationManager.cancelAll()
                    }
                }
            }
            notificationManager.notify(Constants.NOTIFICATION_ID, notificationBuilder)
        }

    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}