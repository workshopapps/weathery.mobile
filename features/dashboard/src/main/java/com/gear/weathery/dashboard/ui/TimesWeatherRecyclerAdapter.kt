package com.gear.weathery.dashboard.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.gear.weathery.dashboard.databinding.WeatherForTimesListItemBinding
import com.gear.weathery.dashboard.models.UITimesWeather
import com.gear.weathery.dashboard.ui.TimesWeatherRecyclerAdapter.TimesWeatherViewHolder

class TimesWeatherRecyclerAdapter: RecyclerView.Adapter<TimesWeatherViewHolder>() {

    private var items = listOf<UITimesWeather>()
    fun updateItemList(items: List<UITimesWeather>){
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

    class TimesWeatherViewHolder(var binding: WeatherForTimesListItemBinding): ViewHolder(binding.root){
        fun bind(uiTimesWeather: UITimesWeather){
            binding.apply {
                weatherTextView.text = uiTimesWeather.weather
                weatherIconImageView.setImageResource(uiTimesWeather.iconResrcId)
                timeTextView.text = uiTimesWeather.time
            }
        }
    }
}