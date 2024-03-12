package com.asifddlks.weatherapp.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.asifddlks.weatherapp.R
import com.asifddlks.weatherapp.data.models.CityUpdate
import com.asifddlks.weatherapp.data.models.ResponseWeather
import com.asifddlks.weatherapp.data.repository.local.CityRepository
import com.asifddlks.weatherapp.data.repository.local.LocationProvider
import com.asifddlks.weatherapp.data.repository.remote.WeatherRepository
import com.asifddlks.weatherapp.databinding.ActivityMainBinding
import com.asifddlks.weatherapp.db.CityDatabase
import com.asifddlks.weatherapp.utils.GPS_REQUEST
import com.asifddlks.weatherapp.utils.GpsUtils
import com.asifddlks.weatherapp.utils.LOCATION_REQUEST
import com.asifddlks.weatherapp.utils.Status
import com.asifddlks.weatherapp.utils.showToast
import com.asifddlks.weatherapp.utils.unixTimestampToTimeString
import com.asifddlks.weatherapp.viewmodel.MyViewModel

/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var viewModel:MyViewModel
    private lateinit var model: LocationProvider
    private lateinit var weatherRepo:WeatherRepository
    private lateinit var cityRepo:CityRepository
    private var isGPSEnabled = false
    private var lat:String?=null
    private var lon:String?=null
    private var city:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        model = LocationProvider(this)
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        weatherRepo = WeatherRepository()
        cityRepo = CityRepository(CityDatabase(this))

        //checking GPS status
        GpsUtils(this).turnGPSOn(object : GpsUtils.OnGpsListener {

            override fun gpsStatus(isGPSEnable: Boolean) {
                this@MainActivity.isGPSEnabled = isGPSEnable
            }
        })

        setUpObservers()
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }

    private fun setUpObservers() {
        viewModel.locationLiveData.observe(this) {
            viewModel.getWeatherByLocation(
                weatherRepo,
                it.latitude.toString(),
                it.longitude.toString()
            )
        }

        viewModel.weatherByLocation.observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.incInfoWeather.root.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        binding.animFailed.visibility = View.GONE
                        binding.animNetwork.visibility = View.GONE
                        setUpUI(it.data)
                        viewModel.updateSavedCities(cityRepo, CityUpdate(it.data?.id, 1))
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPSEnabled = true
                invokeLocationAction()
            }
        }
    }

    private fun invokeLocationAction() {
        when {
            !isGPSEnabled -> showToast(this,"Enable GPS",1)

            isPermissionsGranted() -> startLocationUpdate()

            shouldShowRequestPermissionRationale() -> requestLocationPermission()

            else -> requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_REQUEST
        )
    }

    private fun startLocationUpdate() {
        viewModel.getCurrentLocation(model)
    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }

    fun onAddButtonClicked(view: View) {
        startActivity(Intent(this@MainActivity,SavedCityActivity::class.java))
    }

    fun onForecastButtonClicked(view: View) {
        startActivity(Intent(this@MainActivity,ForecastActivity::class.java)
            .putExtra(ForecastActivity.LATITUDE,lat)
            .putExtra(ForecastActivity.LONGITUDE,lon)
            .putExtra(ForecastActivity.CITY_NAME,city))
    }
}