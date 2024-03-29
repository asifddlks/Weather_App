package com.asifddlks.weatherapp.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import com.asifddlks.weatherapp.BuildConfig
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

fun debug(tag:String,message:String) {
    if (BuildConfig.DEBUG) Log.d(tag,message)
}
fun error(tag:String,message:String) {
    if (BuildConfig.DEBUG) Log.e(tag,message)
}
fun info(tag:String,message:String) {
    if (BuildConfig.DEBUG) Log.i(tag,message)
}

fun lightStatusBar(activity:Activity,value:Boolean){
    if (value){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}

fun showToast(context:Context,message:String,length:Int) {
    Toast.makeText(context,message,length).show()
}

fun isNetworkActive(context: Context):Boolean{
    val isConnected:Boolean
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo

    isConnected = activeNetwork !=null
    return isConnected
}

fun Int.unixTimestampToDateTimeString() : String {

    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this*1000.toLong()

        val outputDateFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault() // user's default time zone
        return outputDateFormat.format(calendar.time)

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toString()
}

fun Int.unixTimestampToTimeString() : String {

    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this*1000.toLong()

        val outputDateFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        return outputDateFormat.format(calendar.time)

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toString()
}