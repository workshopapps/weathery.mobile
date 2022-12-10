package com.gear.weathery.common.`translation-api`

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface API {

    @POST("/translate")
    fun postLang(@Body jsonrequest: JsonrequestItem?
                 , @Query("api-version") apiVersion: String, @Query("from") from: String, @Query("to") to: String, @Header("Content-Type") contentType: String, @Header("Ocp-Apim-Subscription-Key") appKey: String): Call<Translations?>?

}