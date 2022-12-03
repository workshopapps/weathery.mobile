package com.gear.add_remove_location.domain.repository

import com.gear.weathery.common.utils.Resource
import com.gear.weathery.location.api.Location

interface LocationFeatureRepo {
    suspend fun getLocations(query: String):Resource<List<Location>>
}