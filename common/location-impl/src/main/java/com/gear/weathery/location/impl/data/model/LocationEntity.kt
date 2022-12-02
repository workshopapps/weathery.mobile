package com.gear.weathery.location.impl.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
internal data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val state: String,
    val country: String,
    val longitude: Double,
    val latitude: Double
)