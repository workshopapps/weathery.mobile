package com.gear.add_remove_location.data.responses.locations

import com.gear.add_remove_location.data.responses.dto.LocationDto
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationsApi {
    @GET("search")
    suspend fun getLocations(
        @Query("name") name: String
    ): LocationDto

    companion object{
        const val BASE_URL_SEARCH = "https://geocoding-api.open-meteo.com/v1/"
    }
}