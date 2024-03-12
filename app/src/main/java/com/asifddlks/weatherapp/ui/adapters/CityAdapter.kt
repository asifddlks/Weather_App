package com.asifddlks.weatherapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.asifddlks.weatherapp.data.models.Cities
import com.asifddlks.weatherapp.databinding.ItemCitiesBinding
import com.asifddlks.weatherapp.utils.DiffUtilCallback


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

class CityAdapter:RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCitiesBinding) : RecyclerView.ViewHolder(binding.root)

    val differ = AsyncListDiffer(this, DiffUtilCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityAdapter.ViewHolder {
        val binding = ItemCitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityAdapter.ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityAdapter.ViewHolder, position: Int) {
        val cities = differ.currentList[position]
        bindData(holder,cities)
    }

    private fun bindData(holder: CityAdapter.ViewHolder, cities: Cities?) {
        holder.binding.apply {
            tvCityName.text = cities?.name
            tvCountryName.text = cities?.country
            if (cities?.isSaved==1) {
                ivAddCity.visibility=View.GONE
                tvAdded.visibility=View.VISIBLE
            } else {
                tvAdded.visibility=View.GONE
                ivAddCity.visibility=View.VISIBLE
            }
            ivAddCity.setOnClickListener {
                onItemClickListener?.let { it(cities!!) }
                tvAdded.visibility=View.VISIBLE
                ivAddCity.visibility=View.GONE
            }
            root.setOnClickListener {
                onParentItemClickListener?.let { it(cities!!) }
            }
        }
    }

    private var onItemClickListener: ((Cities)->Unit)?=null
    private var onParentItemClickListener: ((Cities)->Unit) ? =null

    fun setOnItemClickListener(listener: (Cities)->Unit){
        onItemClickListener = listener
    }

    fun setOnParentClickListener(listener: (Cities) -> Unit) {
        onParentItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}