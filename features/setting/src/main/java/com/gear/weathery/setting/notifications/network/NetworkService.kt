package com.gear.weathery.setting.notifications.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

private const val BASE_URL = "https://api.tropicalweather.hng.tech/"

val interceptor = HttpLoggingInterceptor()
val client: OkHttpClient =
    OkHttpClient.Builder().addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

interface NetworkService {
    @GET("weather/alerts/subscribe")
    suspend fun subscribeNotifications(
        @Query("fcm_id") fcmId: String,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double
    ): String
}

object NetworkApi {
    val retrofitService: NetworkService by lazy {
        retrofit.create(NetworkService::class.java)
    }

}