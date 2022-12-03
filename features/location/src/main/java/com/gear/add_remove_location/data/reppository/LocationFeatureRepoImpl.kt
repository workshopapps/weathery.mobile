package com.gear.add_remove_location.data.reppository

import com.gear.add_remove_location.data.responses.locations.LocationsApi
import com.gear.add_remove_location.domain.repository.LocationFeatureRepo
import com.gear.weathery.common.utils.Resource
import com.gear.weathery.location.api.Location
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LocationFeatureRepoImpl @Inject constructor(
    private val api:LocationsApi
): LocationFeatureRepo {
    override suspend fun getLocations(query: String): Resource<List<Location>> {
        return try {
            val response = api.getLocations(query)
            Resource.Success(
                data = response.results.map { it.toLocations() }
            )
        } catch (e: IOException){
            Resource.Error(
                message = "Couldn't reach server, check your internet connection",
            )
        } catch (e: HttpException){
            Resource.Error(
                message = e.localizedMessage ?: e.message()
            )
        }
    }
}