package com.asifddlks.weatherapp.ui.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.asifddlks.weatherapp.R
import com.asifddlks.weatherapp.data.models.Daily
import com.asifddlks.weatherapp.databinding.ItemForecastBinding
import com.asifddlks.weatherapp.utils.DiffUtilCallbackForecast
import com.asifddlks.weatherapp.utils.unixTimestampToDateTimeString
import com.asifddlks.weatherapp.utils.unixTimestampToTimeString
import com.bumptech.glide.Glide


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

class ForecastAdapter:RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemForecastBinding) : RecyclerView.ViewHolder(binding.root)

    val differ = AsyncListDiffer(this, DiffUtilCallbackForecast())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastAdapter.ViewHolder {
       val binding = ItemForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastAdapter.ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastAdapter.ViewHolder, position: Int) {
        val data = differ.currentList[position]
        bindData(holder,data)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(holder: ForecastAdapter.ViewHolder, data: Daily?) {
        val weatherConditionIconUrl = "http://openweathermap.org/img/w/${data!!.weather[0].icon}.png"
        holder.binding.apply {
            tvTimeForecast.text = data.dt.unixTimestampToDateTimeString()
            if(!(ivWeatherIcon.context as Activity).isFinishing) Glide.with(ivWeatherIcon.context).load(weatherConditionIconUrl).into(ivWeatherIcon)
            tvWeatherCondition.text = data.weather[0].main
            cvDayForecast.tvDayTemp.text = "Day\n${data.temp.day}${root.context.getString(R.string.degree_celsius_symbol)}"
            cvDayForecast.tvEveTemp.text = "Evening\n${data.temp.eve}${root.context.getString(R.string.degree_celsius_symbol)}"
            cvDayForecast.tvNightTemp.text = "Night\n${data.temp.night}${root.context.getString(R.string.degree_celsius_symbol)}"
            cvDayForecast.tvMaxTemp.text = "Max\n${data.temp.max}${root.context.getString(R.string.degree_celsius_symbol)}"
            cvDayForecast.tvMinTemp.text = "Min\n${data.temp.min}${root.context.getString(R.string.degree_celsius_symbol)}"

            cvDayForecast.tvMornFeel.text = "Morning\n${data.feelsLike.morn}${root.context.getString(R.string.degree_celsius_symbol)}"
            cvDayForecast.tvDayFeel.text = "Day\n${data.feelsLike.day}${root.context.getString(R.string.degree_celsius_symbol)}"
            cvDayForecast.tvEveFeel.text = "Evening\n${data.feelsLike.eve}${root.context.getString(R.string.degree_celsius_symbol)}"
            cvDayForecast.tvNightFeel.text = "Night\n${data.feelsLike.night}${root.context.getString(R.string.degree_celsius_symbol)}"

            cvDayForecast.tvSunriseTime.text = data.sunrise.unixTimestampToTimeString()
            cvDayForecast.tvSunsetTime.text = data.sunset.unixTimestampToTimeString()
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}