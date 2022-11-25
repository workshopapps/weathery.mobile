package com.gear.add_remove_location.di

import com.gear.add_remove_location.data.reppository.LocationFeatureRepoImpl
import com.gear.add_remove_location.data.responses.locations.LocationsApi
import com.gear.add_remove_location.domain.repository.LocationFeatureRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationFeatureModule {

    @Provides
    @Singleton
    fun provideLocationsApi(): LocationsApi{
        return Retrofit.Builder()
            .baseUrl(LocationsApi.BASE_URL_SEARCH)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: LocationsApi): LocationFeatureRepo {
        return LocationFeatureRepoImpl(
            api = api
        )
    }
}