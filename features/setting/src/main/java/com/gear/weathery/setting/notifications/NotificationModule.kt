package com.gear.weathery.setting.notifications

import com.gear.weathery.common.navigation.SettingsNavigation
import com.gear.weathery.setting.SettingsNavigationImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NotificationModule {
    @Singleton
    @Provides
    fun provideSettingsNavigation(): SettingsNavigation = SettingsNavigationImpl()
}