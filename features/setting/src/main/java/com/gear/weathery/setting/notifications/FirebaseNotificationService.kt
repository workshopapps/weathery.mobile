package com.gear.weathery.setting.notifications

import com.gear.weathery.setting.notifications.database.NotificationDao
import com.gear.weathery.setting.notifications.model.NotificationData
import com.gear.weathery.setting.notifications.network.NetworkApi
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
    override fun onMessageReceived(message: RemoteMessage) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            super.onMessageReceived(message)
            val random = message.data

            val event = random.get("event")
            val notif = random.get("message")
            val time = random.get("datetime")

            val notification = NotificationData(notificationText = notif!!, notificationTime = time!!, notificationEvent = event!!)
            notificationDao.insert(notification)
        }

    }



    override fun onNewToken(token: String) {
        super.onNewToken(token)
//        val dummyLng = 1384.90
//        val dummyLat = 12.839
//        val scope = CoroutineScope(Job() + Dispatchers.Main)
//        scope.launch {
//            NetworkApi.retrofitService.subscribeNotifications(token, dummyLat, dummyLng)
//
//        }
    }
}