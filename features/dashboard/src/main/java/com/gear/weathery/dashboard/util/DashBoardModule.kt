package com.gear.weathery.dashboard.util

import com.gear.weathery.common.navigation.DashBoardNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DashBoardModule {
    @Singleton
    @Provides
    fun provideDashBoardNavigation():DashBoardNavigation = DashBoardNavigationImpl()
}