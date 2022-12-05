package com.gear.weathery.setting.notifications.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.gear.weathery.setting.notifications.model.NotificationData
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao  {
    @Insert
    suspend fun insert(vararg notificationData: NotificationData)

    @Query("SELECT * FROM notifications")
    fun getNotifications() : Flow<List<NotificationData>>

    @Query("DELETE FROM notifications")
    suspend fun deleteNotifications()
}