package com.gear.weathery.dashboard.request

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gear.weathery.dashboard.models.WeatherResponse
import com.gear.weathery.dashboard.util.AppExecutor
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class WeatherApiClient {
    private var weatherObject: MutableLiveData<List<WeatherResponse>?> = MutableLiveData()

    private var retrieveWeatherRunnable: RetrieveWeatherRunnable? = null

    companion object{
        private var instance: WeatherApiClient? = null
        fun getInstance(): WeatherApiClient {
            if (instance == null) {
                instance = WeatherApiClient()
            }
            return instance as WeatherApiClient
        }
    }

    fun getWeatherInfo(): LiveData<List<WeatherResponse>?> {
        return weatherObject
    }

    fun getWeatherApi(lat: Int , lon : Int){
        if(retrieveWeatherRunnable != null){
            retrieveWeatherRunnable = null
        }
        retrieveWeatherRunnable = RetrieveWeatherRunnable(lat,lon)

        val myHandler: Future<*> =
            AppExecutor.getInstance().networkIO()!!.submit(retrieveWeatherRunnable)
        AppExecutor.getInstance().networkIO()!!.schedule(Runnable { //Canceling the retrofit call
            myHandler.cancel(true)
        }, 5000, TimeUnit.MILLISECONDS)

    }



    private inner class RetrieveWeatherRunnable : Runnable{
        var  queryLong: Int
        var  queryLat: Int
        var cancelRequest: Boolean=false


        constructor(queryLat:Int, queryLong : Int) {
            this.queryLong = queryLong
            this.queryLat = queryLat
            cancelRequest = false;
        }

        override fun run() {
            try {
                val response: Response<*> = getWeatherInfo(queryLat,queryLong).execute()

                if (cancelRequest) {
                    return
                }

                if (response.code() == 200) {
                    val weatherResponse = response.body() as List<WeatherResponse>?
                    weatherObject.postValue(weatherResponse)
                }

                else {
                    Log.d("WeatherClientApiError", "run: ${response.errorBody().toString()}")
                }

            }
            catch (e : IOException){
                e.printStackTrace()
                weatherObject.postValue(null)

            }

        }
        fun getWeatherInfo(lat: Int, lon:Int): Call<List<WeatherResponse>> {
            return Service.getWeatherApi().getWeatherInfo(lat,lon)
        }




        private fun setCancelRequest() {
            Log.d("CancelReq", "setCancelRequest: Cancelling Search Request")
            cancelRequest = true
        }

    }









}