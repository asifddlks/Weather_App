package com.asifddlks.weatherapp.network

import com.asifddlks.weatherapp.data.models.ResponseWeather
import com.asifddlks.weatherapp.data.models.ResponseWeatherForecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

interface ApiInterface {

    @GET("weather")
   suspend fun getWeatherByLocation(
        @Query("lat")
        latitude:String,
        @Query("lon")
        longitude:String
    ):Response<ResponseWeather>

    @GET("weather")
    suspend fun getWeatherByCityID(
        @Query("id")
        query:String
    ):Response<ResponseWeather>

    @GET("onecall")
    suspend fun getWeatherForecast(
        @Query("lat")
        latitude:String,
        @Query("lon")
        longitude:String,
        @Query("exclude")
        exclude:String
    ):Response<ResponseWeatherForecast>
}