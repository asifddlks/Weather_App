package com.asifddlks.weatherapp.data.repository.local

import com.asifddlks.weatherapp.data.models.Cities
import com.asifddlks.weatherapp.data.models.CityUpdate
import com.asifddlks.weatherapp.db.CityDatabase


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

class CityRepository (private val database: CityDatabase) {

    suspend fun searchCities(key:String) = database.getCityDao().searchCity(key)
    suspend fun updateSavedCities(obj:CityUpdate) = database.getCityDao().updateSavedCity(obj)
    fun getSavedCities(key: Int) = database.getCityDao().getSavedCity(key)
    suspend fun deleteSavedCities(cities: Cities) = database.getCityDao().deleteSavedCity(cities)
}