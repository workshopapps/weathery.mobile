package com.gear.add_remove_location.util

import com.gear.weathery.common.navigation.AddRemoveLocationNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AddRemoveLocationModule {
    @Singleton
    @Provides
    fun provideAddRemoveLocationNavigation(): AddRemoveLocationNavigation = AddRemoveLocationNavigationImpl()
}