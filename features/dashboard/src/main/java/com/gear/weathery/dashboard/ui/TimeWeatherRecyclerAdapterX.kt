package com.gear.weathery.dashboard.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gear.weathery.dashboard.R
import com.gear.weathery.dashboard.databinding.WeatherForTimesListItemBinding
import com.gear.weathery.dashboard.models.TimeWeather
import com.gear.weathery.dashboard.models.UITimesWeather
import com.gear.weathery.dashboard.ui.TimeWeatherRecyclerAdapterX.TimesWeatherViewHolder

class TimeWeatherRecyclerAdapterX: RecyclerView.Adapter<TimesWeatherViewHolder>() {

    private var items = listOf<TimeWeather>()
    fun updateItemList(items: List<TimeWeather>){
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimesWeatherViewHolder {
        return TimesWeatherViewHolder(WeatherForTimesListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: TimesWeatherViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class TimesWeatherViewHolder(var binding: WeatherForTimesListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(timeWeather: TimeWeather){
            binding.apply {
                timeTextView.text = timeWeather.time
                weatherTextView.text = timeWeather.main
                val weatherIcon = when(timeWeather.main){
                    "clear" -> R.drawable.sun_orange
                    "rain" -> R.drawable.rain_4
                    else -> R.drawable.heavy_rain
                }
                weatherIconImageView.setImageResource(weatherIcon)
            }
        }
    }
}