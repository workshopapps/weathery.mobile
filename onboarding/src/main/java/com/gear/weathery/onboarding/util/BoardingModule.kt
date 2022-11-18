package com.gear.weathery.onboarding.util

import com.gear.weathery.common.navigation.BoardingNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BoardingModule {
    @Singleton
    @Provides
    fun provideBoardingNavigation(): BoardingNavigation = BoardingNavigationImpl()
}