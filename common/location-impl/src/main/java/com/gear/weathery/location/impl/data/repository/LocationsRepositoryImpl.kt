package com.gear.weathery.location.impl.data.repository

import com.gear.weathery.location.api.Location
import com.gear.weathery.location.api.LocationsRepository
import com.gear.weathery.location.impl.data.local.LocationDao
import com.gear.weathery.location.impl.data.mappers.toEntity
import com.gear.weathery.location.impl.data.mappers.toLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LocationsRepositoryImpl @Inject constructor(
    private val local: LocationDao
): LocationsRepository {
    override val locations: Flow<List<Location>>
        get() = local.getLocations().map { locations ->
            locations.map { it.toLocation() }
        }

    override suspend fun saveLocation(vararg location: Location) {
        local.save(*location.map { it.toEntity() }.toTypedArray())
    }

    override suspend fun deleteLocation(vararg location: Location) {
        local.delete(*location.map { it.toEntity() }.toTypedArray())
    }
}