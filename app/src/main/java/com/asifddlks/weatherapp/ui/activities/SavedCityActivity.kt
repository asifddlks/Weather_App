package com.asifddlks.weatherapp.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Pair
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asifddlks.weatherapp.R
import com.asifddlks.weatherapp.data.models.CityUpdate
import com.asifddlks.weatherapp.data.repository.local.CityRepository
import com.asifddlks.weatherapp.databinding.ActivitySavedCityBinding
import com.asifddlks.weatherapp.db.CityDatabase
import com.asifddlks.weatherapp.ui.adapters.SavedCityAdapter
import com.asifddlks.weatherapp.utils.RecyclerItemTouchHelper
import com.asifddlks.weatherapp.utils.lightStatusBar
import com.asifddlks.weatherapp.viewmodel.MyViewModel
import com.google.android.material.snackbar.Snackbar


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

class SavedCityActivity : AppCompatActivity(),RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private val binding: ActivitySavedCityBinding by lazy { ActivitySavedCityBinding.inflate(layoutInflater) }

    private lateinit var viewModel : MyViewModel
    private lateinit var repository: CityRepository
    private lateinit var mAdapter : SavedCityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor=resources.getColor(android.R.color.white)
        lightStatusBar(this,true)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        repository = CityRepository(CityDatabase(this))
        mAdapter = SavedCityAdapter()

        setUpRecyclerView()
        setUpObservers()
    }

    private fun setUpObservers() {
        viewModel.getSavedCities(repository,1).observe(this) { cities ->
            mAdapter.differ.submitList(cities)
        }
    }

    private fun setUpRecyclerView() {
        binding.rvSavedCity.apply {
            layoutManager = LinearLayoutManager(this@SavedCityActivity)
            setHasFixedSize(true)
            adapter = mAdapter
        }

        ItemTouchHelper(RecyclerItemTouchHelper(this@SavedCityActivity)).attachToRecyclerView(binding.rvSavedCity)

        mAdapter.setOnItemClickListener {
            startActivity(Intent(this@SavedCityActivity,WeatherDetailsActivity::class.java).putExtra(WeatherDetailsActivity.CITY_ID,it.id.toString()))
        }
    }

    fun onSearchTextClicked(view: View) {
        val intent = Intent(this@SavedCityActivity,SearchActivity::class.java)
        val options = ActivityOptions.makeSceneTransitionAnimation(this, Pair.create(binding.tvCitySearch,getString(R.string.label_search_hint)))
        startActivity(intent,options.toBundle())
        Handler(Looper.myLooper()!!).postDelayed({ finish() }, 1000)
    }

    fun onBackButtonClicked(view: View) {
        onBackPressed()
        finish()
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is SavedCityAdapter.ViewHolder) {
            val pos = viewHolder.adapterPosition
            val cities = mAdapter.differ.currentList[pos]
            viewModel.updateSavedCities(CityRepository(CityDatabase(this@SavedCityActivity)),
                CityUpdate(cities.id,0)
            )

            Snackbar.make(binding.clParent,"City removed from saved items",Snackbar.LENGTH_LONG).apply {
                setAction("Undo"){
                    viewModel.updateSavedCities(CityRepository(CityDatabase(this@SavedCityActivity)),
                        CityUpdate(cities.id,1)
                    )
                }
                setBackgroundTint(resources.getColor(R.color.colorPrimary))
                setActionTextColor(resources.getColor(R.color.color_grey))
                show()
            }

        }

    }

}