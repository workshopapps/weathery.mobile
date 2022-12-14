package com.gear.add_remove_location.data.repository

import com.gear.add_remove_location.R
import com.gear.add_remove_location.data.responses.locations.LocationsApi
import com.gear.add_remove_location.domain.repository.LocationFeatureRepo
import com.gear.add_remove_location.presentation.manage_location.util.LocationResource
import com.gear.add_remove_location.presentation.manage_location.util.UIText
import com.gear.weathery.location.api.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class LocationFeatureRepoImpl @Inject constructor(
    private val api: LocationsApi
) : LocationFeatureRepo {
    override suspend fun getLocations(query: String): Flow<LocationResource<List<Location>>> =
        flow {
            emit(LocationResource.Loading())

            try {
                val response = api.getLocations(query)
                if (response.results == null) {
                    emit(LocationResource.Error(message = UIText.StringResource(R.string.error_invalid_location)))
                } else {
                    emit(LocationResource.Success(
                        data = response.results.map { it.toLocations() }
                    ))
                }
            } catch (e: IOException) {
                emit(LocationResource.Error(
                    message = UIText.StringResource(R.string.error_could_not_reach_server),
                ))
            } catch (e: HttpException) {
                when (e.code()) {
                    // not found
                    HttpURLConnection.HTTP_NOT_FOUND -> emit(LocationResource.Error(
                        message = UIText.DynamicString(e.message())
                    ))
                    else -> {
                        emit(LocationResource.Error(
                            message = UIText.StringResource(R.string.error_try_again_later)
                        ))
                    }
                }
            }

        }
}