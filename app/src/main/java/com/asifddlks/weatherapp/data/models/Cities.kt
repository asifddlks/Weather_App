package com.asifddlks.weatherapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.asifddlks.weatherapp.utils.TABLE_CITY


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

@Entity(
    tableName = TABLE_CITY
)
data class Cities (
    @PrimaryKey(autoGenerate = false)
    var id:Int?=null,

    @ColumnInfo(name = "name")
    var name:String?=null,

    @ColumnInfo(name = "state")
    var state:String?=null,

    @ColumnInfo(name = "country")
    var country:String?=null,

    @ColumnInfo(name = "coord_lon")
    var lon:Double?=null,

    @ColumnInfo(name = "coord_lat")
    var lat:Double?=null,

    @ColumnInfo(name = "isSaved")
    var isSaved:Int?=null
)