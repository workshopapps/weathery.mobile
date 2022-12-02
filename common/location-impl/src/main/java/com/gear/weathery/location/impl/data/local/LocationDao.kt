package com.gear.weathery.location.impl.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gear.weathery.location.impl.data.model.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg location: LocationEntity)

    @Query("SELECT * FROM locations")
    fun getLocations(): Flow<List<LocationEntity>>

    @Delete
    suspend fun delete(vararg location: LocationEntity)
}