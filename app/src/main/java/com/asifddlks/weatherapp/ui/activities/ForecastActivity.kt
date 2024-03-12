package com.asifddlks.weatherapp.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.asifddlks.weatherapp.data.repository.remote.WeatherRepository
import com.asifddlks.weatherapp.databinding.ActivityForecastBinding
import com.asifddlks.weatherapp.ui.adapters.ForecastAdapter
import com.asifddlks.weatherapp.utils.Status
import com.asifddlks.weatherapp.utils.lightStatusBar
import com.asifddlks.weatherapp.viewmodel.MyViewModel

/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

class ForecastActivity : AppCompatActivity() {

    private val binding: ActivityForecastBinding by lazy { ActivityForecastBinding.inflate(layoutInflater) }

    private lateinit var viewModel: MyViewModel
    private lateinit var repository: WeatherRepository
    private lateinit var mAdapter: ForecastAdapter
    private var lat:String?=null
    private var lon:String?=null
    private var city:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor=resources.getColor(android.R.color.white)
        lightStatusBar(this,true)
        setContentView(binding.root)


        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        repository = WeatherRepository()
        mAdapter = ForecastAdapter()

        lat = intent.getStringExtra(LATITUDE)
        lon = intent.getStringExtra(LONGITUDE)
        city = intent.getStringExtra(CITY_NAME)

        binding.toolLayout.tvToolTitle.text = city

        if (lat!=null && lon!=null) viewModel.getWeatherForecast(repository,lat!!,lon!!,EXCLUDE)

        setUpRecyclerView()
        setUpObservers()

    }

    private fun setUpObservers() {
        viewModel.weatherForecast.observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvErrorMsg.visibility = View.GONE
                        binding.animFailed.visibility = View.GONE
                        binding.animFailed.visibility = View.GONE
                        binding.rvForecast.visibility = View.VISIBLE
                        mAdapter.differ.submitList(it.data?.daily)
                    }

                    Status.ERROR -> {
                        showFailedView(it.message)
                    }

                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.tvErrorMsg.visibility = View.GONE
                        binding.rvForecast.visibility = View.GONE
                        binding.animFailed.visibility = View.GONE
                        binding.animNetwork.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun showFailedView(message: String?) {
        binding.progressBar.visibility=View.GONE
        binding.tvErrorMsg.visibility=View.GONE
        binding.rvForecast.visibility=View.GONE

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

    private fun setUpRecyclerView() {
        binding.rvForecast.apply {
            layoutManager = LinearLayoutManager(this@ForecastActivity)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    fun onBackButtonClicked(view: View) {
        onBackPressed()
        finish()
    }


    companion object {
        const val LATITUDE = "lat"
        const val LONGITUDE = "lon"
        const val CITY_NAME = "city"
        const val EXCLUDE = "current,minutely,hourly"
    }
}