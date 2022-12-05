package com.gear.weathery.setting.notifications.di

import android.app.Application
import androidx.room.Room
import com.gear.weathery.setting.notifications.database.NotificationDao
import com.gear.weathery.setting.notifications.database.NotificationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class NotificationModule {

    @Provides
    @Singleton
    fun provideNotificationDatabase(app: Application): NotificationDatabase {
        val database: NotificationDatabase by lazy {
            NotificationDatabase.getDatabase(app)
        }
        return database
    }

    @Provides
    fun notificationDao(db: NotificationDatabase): NotificationDao = db.notificationDao()
}