package com.asifddlks.weatherapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.asifddlks.weatherapp.data.models.Cities
import com.asifddlks.weatherapp.data.models.Daily


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

class  DiffUtilCallback:DiffUtil.ItemCallback<Cities>() {
    override fun areItemsTheSame(oldItem: Cities, newItem: Cities): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: Cities, newItem: Cities): Boolean {
        return oldItem==newItem
    }
}

class DiffUtilCallbackForecast:DiffUtil.ItemCallback<Daily>(){
    override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem.dt==newItem.dt
    }

    override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem==newItem
    }

}