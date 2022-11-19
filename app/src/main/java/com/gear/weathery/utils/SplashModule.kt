package com.gear.weathery.utils

import com.gear.weathery.common.navigation.SplashNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SplashModule {

    @Singleton
    @Provides
    fun provideSplashNavigation():SplashNavigation= SplashNavigationImpl()
}