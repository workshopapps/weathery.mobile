package com.gear.add_remove_location.domain.repository

import com.gear.add_remove_location.presentation.manage_location.util.LocationResource
import com.gear.weathery.location.api.Location

interface LocationFeatureRepo {
    suspend fun getLocations(query: String):LocationResource<List<Location>>
}