package com.gear.weathery.location.impl.di

import com.gear.weathery.location.api.LocationsRepository
import com.gear.weathery.location.impl.data.repository.LocationsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun repository(locationsRepositoryImpl: LocationsRepositoryImpl):LocationsRepository
}