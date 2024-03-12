package com.asifddlks.weatherapp.utils


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

interface RequestCompleteListener<T> {
    fun onRequestCompleted(data:T)
    fun onRequestFailed(errorMessage:String?)
}