package com.gear.weathery.dashboard.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.gear.weathery.dashboard.R
import com.gear.weathery.dashboard.databinding.WeatherListBinding
import com.gear.weathery.dashboard.models.HourWeather
import com.gear.weathery.dashboard.models.getAmPmTime
import com.gear.weathery.dashboard.repository.NONE

class TimelineRecyclerAdapter: RecyclerView.Adapter<TimelineRecyclerAdapter.HourWeatherViewHolder>() {

    private var items = listOf<HourWeather>()
    fun updateItemList(items: List<HourWeather>){
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourWeatherViewHolder {
        return HourWeatherViewHolder(WeatherListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: HourWeatherViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class HourWeatherViewHolder(var binding: WeatherListBinding): ViewHolder(binding.root){
        fun bind(hourWeather: HourWeather){
            binding.apply {
                val time = getAmPmTime(hourWeather.timeInMillis)
                timeTextView.text = "${time.first}:00\n${time.second}"
                weatherDescriptionTextView.text = hourWeather.main
                weatherRiskTextView.text = hourWeather.risk
                weatherIconImageView.setImageResource(
                    when(hourWeather.main){
                        "Drizzle", "Freezing Drizzle", "Freezing rain" -> R.drawable.sun_cloud_rain
                        "Rain", "Rain showers", "Thunderstorm" -> R.drawable.cloud_rain
                        "Fog and depositing rime fog" -> R.drawable.cloud
                        "Scattered clouds", "Few clouds", "Broken clouds" -> R.drawable.sun_cloud
                        else -> R.drawable.sun
                    }
                )
                riskIndicatorImageView.setImageResource(if (hourWeather.risk == NONE) R.drawable.ic_warning_inactive else R.drawable.ic_warning_active)
            }
        }
    }
}