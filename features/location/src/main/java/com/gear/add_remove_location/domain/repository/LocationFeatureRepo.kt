package com.gear.add_remove_location.domain.repository

import com.gear.add_remove_location.data.responses.dto.LocationDtoItem
import com.gear.weathery.common.utils.Resource

interface LocationFeatureRepo {
    suspend fun getLocations(query: String):Resource<List<LocationDtoItem>>
}