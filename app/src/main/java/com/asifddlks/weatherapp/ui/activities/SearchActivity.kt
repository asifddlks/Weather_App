package com.asifddlks.weatherapp.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.database.DatabaseUtils
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Pair
import android.view.KeyEvent
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.asifddlks.weatherapp.R
import com.asifddlks.weatherapp.data.models.Cities
import com.asifddlks.weatherapp.data.models.CityUpdate
import com.asifddlks.weatherapp.data.repository.local.CityRepository
import com.asifddlks.weatherapp.databinding.ActivitySearchBinding
import com.asifddlks.weatherapp.db.CityDatabase
import com.asifddlks.weatherapp.ui.adapters.CityAdapter
import com.asifddlks.weatherapp.utils.Status
import com.asifddlks.weatherapp.utils.lightStatusBar
import com.asifddlks.weatherapp.viewmodel.MyViewModel


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

class SearchActivity : AppCompatActivity() {

    private val binding: ActivitySearchBinding by lazy { ActivitySearchBinding.inflate(layoutInflater) }

    private lateinit var viewModel : MyViewModel
    private lateinit var repository: CityRepository
    private lateinit var database: CityDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor=resources.getColor(android.R.color.white)
        lightStatusBar(this,true)
        window.navigationBarColor=resources.getColor(android.R.color.white)
        setContentView(binding.root)

        database = CityDatabase(this)
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        repository = CityRepository(database)

        binding.svSearchCity.requestFocus()

        setUpUI()
        setUpObservers()
    }

    private fun setUpObservers() {
        viewModel.cityByQuery.observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        if (it.data!!.isNotEmpty()) {
                            binding.pbSearch.visibility = View.GONE
                            binding.rvSearchedResult.visibility = View.VISIBLE
                            //set data to recycler view
                            setUpRecyclerView(it.data)
                        } else {
                            binding.pbSearch.visibility = View.GONE
                            binding.rvSearchedResult.visibility = View.GONE
                            binding.tvNoResult.visibility = View.VISIBLE
                        }
                    }

                    Status.ERROR -> {
                        showFailedView(it.message)
                    }

                    Status.LOADING -> {
                        binding.pbSearch.visibility = View.VISIBLE
                        binding.rvSearchedResult.visibility = View.GONE
                        binding.tvNoResult.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView(data: List<Cities>) {
        val cityAdapter = CityAdapter()
        binding.rvSearchedResult.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            setHasFixedSize(true)
            adapter = cityAdapter
        }
        cityAdapter.differ.submitList(data)

        cityAdapter.setOnItemClickListener { viewModel.updateSavedCities(repository, CityUpdate(it.id,1)) }
        cityAdapter.setOnParentClickListener {
            startActivity(Intent(this@SearchActivity,WeatherDetailsActivity::class.java).putExtra(WeatherDetailsActivity.CITY_ID,it.id.toString()))
        }
    }

    private fun setUpUI() {
        binding.svSearchCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchedQuery = if (query!!.contains("'")) DatabaseUtils.sqlEscapeString(query).replace("'","") else query
                viewModel.getCityByQuery(repository,searchedQuery)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchedQuery = if (newText!!.contains("'")) DatabaseUtils.sqlEscapeString(newText).replace("'","") else newText
                viewModel.getCityByQuery(repository,searchedQuery)
                return false
            }

        })
    }

    private fun showFailedView(message:String?){
        binding.pbSearch.visibility=View.GONE
        binding.rvSearchedResult.visibility=View.GONE
        binding.tvNoResult.visibility=View.VISIBLE
        binding.tvNoResult.text = message
    }

    fun onCancelButtonClicked(view: View) {
        navigateBack()
    }

    private fun navigateBack() {
        val intent = Intent(this@SearchActivity, SavedCityActivity::class.java)
        val options = ActivityOptions.makeSceneTransitionAnimation(
            this,
            Pair.create(binding.svSearchCity, getString(R.string.label_search_hint))
        )
        startActivity(intent, options.toBundle())
        Handler(Looper.myLooper()!!).postDelayed({ finish() }, 1000)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            navigateBack()
        }
        return super.onKeyDown(keyCode, event)
    }
}