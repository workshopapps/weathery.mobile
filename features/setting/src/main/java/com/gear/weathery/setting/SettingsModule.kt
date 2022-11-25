package com.gear.weathery.setting

import com.gear.weathery.common.navigation.NotificationsNavigation
import com.gear.weathery.common.navigation.SettingsNavigation
import com.gear.weathery.setting.notifications.NotificationsNavigationImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SettingsModule {
    @Singleton
    @Provides
    fun provideSettingsNavigation(): SettingsNavigation = SettingsNavigationImpl()

    @Singleton
    @Provides
    fun provideNotificationsNavigation(): NotificationsNavigation = NotificationsNavigationImpl()

}