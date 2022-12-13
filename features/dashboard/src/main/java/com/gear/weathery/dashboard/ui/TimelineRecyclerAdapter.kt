package com.gear.weathery.dashboard.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.gear.weathery.dashboard.R
import com.gear.weathery.dashboard.databinding.WeatherListBinding
import com.gear.weathery.dashboard.models.TimelineWeather
import com.gear.weathery.dashboard.models.getAmPmTime
import com.gear.weathery.dashboard.models.getDateForDisplay
import com.gear.weathery.dashboard.repository.NONE

const val HOURLY_TIMELINE = "hourly timeline"
const val DAILY_TIMELINE = "daily timeline"

class TimelineRecyclerAdapter :
    RecyclerView.Adapter<TimelineRecyclerAdapter.HourWeatherViewHolder>() {

    private var items = listOf<TimelineWeather>()
    private var timeLineType = ""
    fun updateItemList(items: List<TimelineWeather>, timelineType: String) {
        this.items = items
        this.timeLineType = timelineType
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourWeatherViewHolder {
        return HourWeatherViewHolder(WeatherListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: HourWeatherViewHolder, position: Int) {
        holder.bind(items[position], timeLineType)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class HourWeatherViewHolder(var binding: WeatherListBinding) : ViewHolder(binding.root) {
        fun bind(timelineWeather: TimelineWeather, timelineType: String) {
            binding.apply {

                if(adapterPosition == 0){
                    ellipseId.setImageResource(R.drawable.ellipse)
                    dividerLine.setImageResource(R.drawable.line)
                }else{
                    ellipseId.setImageResource(R.drawable.ellipse_white)
                    dividerLine.setImageResource(R.drawable.line_white)
                }

                timeTextView.text =
                    if (timelineType == DAILY_TIMELINE) {
                        val date = getDateForDisplay(timelineWeather.timeInMillis)
                        "${date.first}\n${date.second}"
                    } else {
                        val time = getAmPmTime(timelineWeather.timeInMillis)
                        "${time.first}:00\n${time.second}"
                    }

                weatherDescriptionTextView.text = timelineWeather.main
                weatherRiskTextView.text = if(timelineWeather.risk != "null") timelineWeather.risk else "None"
                weatherIconImageView.setImageResource(
                    when (timelineWeather.main) {
                        "Drizzle", "Freezing Drizzle", "Freezing rain" -> R.drawable.sun_cloud_rain
                        "Rain", "Rain showers", "Thunderstorm" -> R.drawable.cloud_rain
                        "Fog and depositing rime fog" -> R.drawable.cloud
                        "Scattered clouds", "Few clouds", "Broken clouds" -> R.drawable.sun_cloud
                        else -> R.drawable.sun
                    }
                )
                riskIndicatorImageView.setImageResource(if (timelineWeather.risk == NONE) R.drawable.ic_warning_inactive else R.drawable.ic_warning_active)
            }
        }
    }
}