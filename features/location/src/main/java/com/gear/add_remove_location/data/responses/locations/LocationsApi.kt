package com.gear.add_remove_location.data.responses.locations

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface LocationsApi {
    @Headers("X-Api-Key: kV+PztlO5ecxx5wqi5p2cw==w2at5sjz5gPOgi5U")
    @GET("geocoding")
    suspend fun getLocations(
        @Query("city") city: String = "Nairobi"
    ): List<LocationDtoItem>

    companion object{
        const val BASE_URL_SEARCH = "https://api.api-ninjas.com/v1/"
    }
}