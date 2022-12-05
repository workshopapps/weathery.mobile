package com.gear.weathery.setting.notifications.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

private const val BASE_URL = "https://63d0-105-112-104-47.eu.ngrok.io/"


    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    interface NetworkService{
        @GET("weather/alerts/subscribe")
        suspend fun subscribeNotifications(@Query("fcm_id")fcmId: String, @Query("lat")lat: Double, @Query("lng")lng: Double)
    }

    object NetworkApi{
        val retrofitService: NetworkService by lazy {
            retrofit.create(NetworkService::class.java)
        }

}