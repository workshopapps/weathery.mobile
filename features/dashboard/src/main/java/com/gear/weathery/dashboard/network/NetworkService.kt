package com.gear.weathery.dashboard.network

import com.gear.weathery.dashboard.models.NWTimeWeather
import com.gear.weathery.dashboard.models.TimeWeather
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.json.JSONArray
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.weathery.hng.tech"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface NetworkService{
    @GET("weather/forecasts")
    suspend fun getWeathers(@Query("lat")lat: Int, @Query("lng")lon: Int): List<NWTimeWeather>
}

object NetworkApi{
    val retrofitService: NetworkService by lazy {
        retrofit.create(NetworkService::class.java)
    }
}