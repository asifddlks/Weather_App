package com.asifddlks.weatherapp.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.asifddlks.weatherapp.R
import com.asifddlks.weatherapp.data.models.ResponseWeather
import com.asifddlks.weatherapp.data.repository.remote.WeatherRepository
import com.asifddlks.weatherapp.databinding.ActivitySplashScreenBinding
import com.asifddlks.weatherapp.databinding.ActivityWeatherDetailsBinding
import com.asifddlks.weatherapp.utils.Status
import com.asifddlks.weatherapp.utils.unixTimestampToTimeString
import com.asifddlks.weatherapp.viewmodel.MyViewModel


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

class WeatherDetailsActivity : AppCompatActivity() {

    private val binding: ActivityWeatherDetailsBinding by lazy { ActivityWeatherDetailsBinding.inflate(layoutInflater) }

    private lateinit var viewModel: MyViewModel
    private lateinit var weatherRepo: WeatherRepository
    private var cityID:String?=null
    private var lat:String?=null
    private var lon:String?=null
    private var city:String?=null

    companion object{
        const val CITY_ID = "city_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        weatherRepo = WeatherRepository()

        binding.incInfoWeather.ivAdd.setImageResource(R.drawable.ic_arrow_back_white)

        cityID = intent.getStringExtra(CITY_ID)

        viewModel.getWeatherByCityID(weatherRepo,cityID!!)

        setUpObservers()

    }

    private fun setUpObservers() {
        viewModel.weatherByCityID.observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.incInfoWeather.root.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        binding.animFailed.visibility = View.GONE
                        binding.animNetwork.visibility = View.GONE
                        setUpUI(it.data)
                    }

                    Status.ERROR -> {
                        showFailedView(it.message)
                    }

                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.animFailed.visibility = View.GONE
                        binding.animNetwork.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun showFailedView(message: String?) {
        binding.progressBar.visibility=View.GONE
        binding.incInfoWeather.root.visibility=View.GONE

        when(message){
            "Network Failure" -> {
                binding.animFailed.visibility=View.GONE
                binding.animNetwork.visibility=View.VISIBLE
            }
            else ->{
                binding.animNetwork.visibility=View.GONE
                binding.animFailed.visibility=View.VISIBLE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpUI(data: ResponseWeather?) {
        binding.incInfoWeather.tvTemp.text = data?.main?.temp.toString()
        binding.incInfoWeather.tvCityName.text = data?.name
        binding.incInfoWeather.tvWeatherCondition.text = data?.weather!![0].main
        binding.incInfoWeather.include2.tvSunriseTime.text = data.sys.sunrise.unixTimestampToTimeString()
        binding.incInfoWeather.include2.tvSunsetTime.text = data.sys.sunset.unixTimestampToTimeString()
        binding.incInfoWeather.include2.tvRealFeelText.text = "${data.main.feelsLike}${getString(R.string.degree_celsius_symbol)}"
        binding.incInfoWeather.include2.tvCloudinessText.text = "${data.clouds.all}%"
        binding.incInfoWeather.include2.tvWindSpeedText.text = "${data.wind.speed}m/s"
        binding.incInfoWeather.include2.tvHumidityText.text = "${data.main.humidity}%"
        binding.incInfoWeather.include2.tvPressureText.text = "${data.main.pressure}hPa"
        binding.incInfoWeather.include2.tvVisibilityText.text = "${data.visibility}M"

        lat = data.coord.lat.toString()
        lon = data.coord.lon.toString()
        city = data.name
    }

    fun onAddButtonClicked(view: View) {
        onBackPressed()
        finish()
    }

    fun onForecastButtonClicked(view: View) {
        startActivity(Intent(this@WeatherDetailsActivity,ForecastActivity::class.java)
            .putExtra(ForecastActivity.LATITUDE,lat)
            .putExtra(ForecastActivity.LONGITUDE,lon)
            .putExtra(ForecastActivity.CITY_NAME,city))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}