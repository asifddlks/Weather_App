package com.asifddlks.weatherapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

@Entity
data class CityUpdate (
    @ColumnInfo(name = "id")
    var id:Int?=null,

    @ColumnInfo(name = "isSaved")
    var isSaved:Int?=null
)