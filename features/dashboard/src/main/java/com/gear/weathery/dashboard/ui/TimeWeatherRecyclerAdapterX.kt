package com.gear.weathery.dashboard.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gear.weathery.dashboard.R
import com.gear.weathery.dashboard.databinding.WeatherForTimesListItemBinding
import com.gear.weathery.dashboard.models.TimeWeather
import com.gear.weathery.dashboard.models.UITimeWeather
import com.gear.weathery.dashboard.ui.TimeWeatherRecyclerAdapterX.TimesWeatherViewHolder

class TimeWeatherRecyclerAdapterX(val onItemClick: (Int) -> Unit): RecyclerView.Adapter<TimesWeatherViewHolder>() {

    private var items = listOf<UITimeWeather>()
    fun updateItemList(items: List<UITimeWeather>){
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimesWeatherViewHolder {
        return TimesWeatherViewHolder(WeatherForTimesListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: TimesWeatherViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener{onItemClick(item.id)}
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class TimesWeatherViewHolder(var binding: WeatherForTimesListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(timeWeather: UITimeWeather){
            binding.apply {
                timeTextView.text = timeWeather.time

                weatherTextView.text = timeWeather.main

                val weatherIcon = when(timeWeather.main){
                    "Clear" -> R.drawable.sun_orange
                    "Clouds" -> R.drawable.rain_4
                    else -> R.drawable.heavy_rain
                }
                weatherIconImageView.setImageResource(weatherIcon)

                containerLinearLayout.setBackgroundResource(if (timeWeather.highlighted) R.drawable.bg_highlight else android.R.color.white)
            }
        }
    }
}