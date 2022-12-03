package com.gear.weathery.location.impl.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
internal data class LocationEntity(
    val name: String,
    val state: String,
    val country: String,
    val longitude: Double,
    val latitude: Double,
    @PrimaryKey(autoGenerate = true)
    val id: Int
)