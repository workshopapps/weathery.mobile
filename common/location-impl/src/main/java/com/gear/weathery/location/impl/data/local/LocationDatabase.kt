package com.gear.weathery.location.impl.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gear.weathery.location.impl.data.model.LocationEntity

@Database(entities = [LocationEntity::class], version = 1, exportSchema = false)
internal abstract class LocationDatabase : RoomDatabase(){

    abstract val dao: LocationDao

    companion object{
        const val DB_NAME = "locations_db"
    }
}