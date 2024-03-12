package com.asifddlks.weatherapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.asifddlks.weatherapp.data.models.Cities
import com.asifddlks.weatherapp.databinding.ItemSavedCityBinding
import com.asifddlks.weatherapp.utils.DiffUtilCallback


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

class SavedCityAdapter:RecyclerView.Adapter<SavedCityAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemSavedCityBinding) : RecyclerView.ViewHolder(binding.root)

    val differ = AsyncListDiffer(this, DiffUtilCallback())
    private var onItemClickListener : ((Cities)->Unit)?=null

    fun setOnItemClickListener(listener: (Cities)->Unit){
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedCityAdapter.ViewHolder {
        val binding = ItemSavedCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedCityAdapter.ViewHolder(binding)
    }

    override fun onBindViewHolder(holder:  SavedCityAdapter.ViewHolder, position: Int) {
        val cities = differ.currentList[position]
        bindData(cities,holder)
    }

    private fun bindData(cities: Cities?, holder: SavedCityAdapter.ViewHolder) {
        holder.binding.apply {
            tvCityNameSearch.text = cities?.name
            tvCountryNameSearch.text = cities?.country
            tvCityTemp.text = ""
            root.setOnClickListener { onItemClickListener?.let { it(cities!!) } }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}