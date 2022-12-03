package com.gear.weathery.location.impl.di

import android.app.Application
import androidx.room.Room
import com.gear.weathery.location.impl.data.local.LocationDao
import com.gear.weathery.location.impl.data.local.LocationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal class LocalModule {

    @Provides
    @Singleton
    fun provideLocationDatabase(app: Application): LocationDatabase{
        return Room.databaseBuilder(
            app,
            LocationDatabase::class.java,
            LocationDatabase.DB_NAME
        ).build()
    }

    @Provides
    fun locationDao(db: LocationDatabase): LocationDao = db.dao
}