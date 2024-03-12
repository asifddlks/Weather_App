package com.asifddlks.weatherapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.asifddlks.weatherapp.data.models.Cities
import com.asifddlks.weatherapp.data.models.CityUpdate


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

@Dao
interface CityDao {

    @Query("SELECT * FROM city_bd WHERE name LIKE :key || '%'")
    suspend fun searchCity(key:String):List<Cities>

    @Update(entity = Cities::class)
    suspend fun updateSavedCity(vararg obj:CityUpdate):Int

    @Query("SELECT * FROM city_bd WHERE isSaved= :key")
    fun getSavedCity(key:Int):LiveData<List<Cities>>

    @Delete
    suspend fun deleteSavedCity(city:Cities)
}