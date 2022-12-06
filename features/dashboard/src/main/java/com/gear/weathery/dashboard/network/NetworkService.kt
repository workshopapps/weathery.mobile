package com.gear.weathery.dashboard.network

import com.gear.weathery.dashboard.models.LinkResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.tropicalweather.hng.tech"
private const val GEO_CODING_BASE_URL = "http://api.geonames.org"
const val URL_TO_SHARE = "https://tropicalweather.hng.tech/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

private val geoCodingRetrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(GEO_CODING_BASE_URL)
    .build()

interface NetworkService{
    @GET("weather/forcast/extended")
    suspend fun getWeatherToday(@Query("lat") lat: Double, @Query("lon") lon: Double): String

    @GET("weather/forecasts/tomorrow")
    suspend fun getWeatherTomorrow(@Query("lat") lat: Double, @Query("lon") lon: Double): String

    @GET("/weather/weekly")
    suspend fun getWeatherThisWeek(@Query("lat") lat: Double, @Query("lon") lon: Double): String
}

interface GeoCodingNetworkService{
    @GET("findNearbyJSON")
    suspend fun getLocationName(@Query("lat") lat: Double, @Query("lng") lon: Double, @Query("username") username: String = "aaction77"): String
}

interface ShareWeather{
    @GET("generate/share-link")
    suspend fun getShareLinkResponse(
        @Query("city") city:String,
        @Query("state") state:String,
        @Query("country") country:String
    ): Response<LinkResponse>
}


object NetworkApi{
    val retrofitService: NetworkService by lazy {
        retrofit.create(NetworkService::class.java)
    }
}

object GeoCodingNetworkApi{
    val geoCodingRetrofitService: GeoCodingNetworkService by lazy {
        geoCodingRetrofit.create(GeoCodingNetworkService::class.java)
    }
}
object ShareLinkNetworkApi{
    val shareLinkRetrofit: ShareWeather by lazy {
        retrofit.create(ShareWeather::class.java)
    }
}