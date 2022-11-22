package com.gear.weathery.utils

import com.gear.weathery.common.navigation.AccountNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AccountProfileModule {
    @Singleton
    @Provides
    fun provideAccountProfileNavigation():AccountNavigation = AccountProfileNavigationImpl()
}