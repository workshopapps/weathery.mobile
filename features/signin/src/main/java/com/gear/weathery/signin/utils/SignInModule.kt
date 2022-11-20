package com.gear.weathery.signin.utils

import com.gear.weathery.common.navigation.SignInNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SignInModule {
    @Singleton
    @Provides
    fun provideSignInNavigation(): SignInNavigation = SignInNavigationImpl()
}