package com.asifddlks.weatherapp.data.repository.remote

import com.asifddlks.weatherapp.network.RetrofitClient

/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

class WeatherRepository {

    suspend fun getWeatherByLocation(lat:String,lon:String) = RetrofitClient.api.getWeatherByLocation(lat,lon)
    suspend fun getWeatherByCityID(id:String) = RetrofitClient.api.getWeatherByCityID(id)
    suspend fun getWeatherForecast(lat: String,lon: String,exclude:String) = RetrofitClient.api.getWeatherForecast(lat,lon,exclude)
}