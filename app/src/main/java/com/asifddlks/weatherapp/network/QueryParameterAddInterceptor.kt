package com.asifddlks.weatherapp.network

import com.asifddlks.weatherapp.WeatherApplication
import com.asifddlks.weatherapp.utils.APP_ID
import com.asifddlks.weatherapp.utils.PrefManager
import okhttp3.Interceptor
import okhttp3.Response


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

class QueryParameterAddInterceptor:Interceptor {

    val context = WeatherApplication.applicationContext()
    private val prefManager = PrefManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url().newBuilder()
            .addQueryParameter("appid", APP_ID)
            .addQueryParameter("units",prefManager.tempUnit)
            .build()

        val request = chain.request().newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}