package com.asifddlks.weatherapp

import android.app.Application
import android.content.Context


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

class WeatherApplication : Application() {

    init {
        instance = this
    }
    companion object {
        var instance: WeatherApplication? = null
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}