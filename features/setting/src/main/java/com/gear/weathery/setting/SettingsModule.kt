package com.gear.weathery.setting

import com.gear.weathery.common.navigation.SettingsNavigation
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
}