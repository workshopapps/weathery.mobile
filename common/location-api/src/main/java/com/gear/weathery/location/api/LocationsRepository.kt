package com.gear.weathery.location.api

import kotlinx.coroutines.flow.Flow

interface LocationsRepository {
    val locations: Flow<List<Location>>

//    suspend fun getLocation(id: Int): Flow<Location>

    suspend fun saveLocation(vararg location: Location)

    suspend fun deleteLocation(vararg location: Location)
}