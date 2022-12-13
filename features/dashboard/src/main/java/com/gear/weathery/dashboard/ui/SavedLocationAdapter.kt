package com.gear.weathery.dashboard.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gear.weathery.dashboard.R
import com.gear.weathery.dashboard.databinding.SavedLocationItemBinding
import com.gear.weathery.location.api.Location
import java.util.*


class SavedLocationAdapter: ListAdapter<Location, SavedLocationAdapter.ViewHolder>(diffObject) {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding =SavedLocationItemBinding.bind(view)
        fun bind(location: Location){

            when{
                location.state.isEmpty() && location.country.isNotEmpty() ->{
                    binding.locationItem.text = "${location.name.capitalize(Locale.ROOT) }, ${location.country}"
                }
                location.name.isEmpty() && location.country.isNotEmpty()->{
                    binding.locationItem.text = "${location.state }, ${location.country}"
                }
                location.name.isNotEmpty() && location.state.isNotEmpty()->{
                    binding.locationItem.text = "${location.name.capitalize(Locale.ROOT) }, ${location.state }"
                }
                else->{
                    binding.locationItem.text = "Current Location"
                }
            }

            binding.root.setOnClickListener{
                listener?.let { it1 -> it1(location) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.saved_location_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = getItem(position)
        if (pos != null)
            holder.bind(pos)
    }
    companion object {
        val diffObject = object : DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem.name == newItem.name
            }
            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem.longitude  == newItem.longitude && oldItem.latitude == newItem.latitude
            }
        }
    }

    private var listener:((Location)->Unit)? = null
    fun adapterClick(listener:(Location)->Unit){
        this.listener = listener
    }
}