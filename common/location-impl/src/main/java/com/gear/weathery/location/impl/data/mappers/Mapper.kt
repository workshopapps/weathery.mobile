package com.gear.weathery.location.impl.data.mappers

import com.gear.weathery.location.api.Location
import com.gear.weathery.location.impl.data.model.LocationEntity

internal fun LocationEntity.toLocation() = Location(
    id = id,
    name = name,
    country = country,
    longitude = longitude,
    latitude = latitude,
    state = state
)

internal fun Location.toEntity() = LocationEntity(
    id = id,
    name = name,
    country = country,
    longitude = longitude,
    latitude = latitude,
    state = state
)