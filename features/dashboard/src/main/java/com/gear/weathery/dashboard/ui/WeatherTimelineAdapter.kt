package com.gear.weathery.dashboard.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gear.weathery.dashboard.R
import com.gear.weathery.dashboard.models.NotificationData
import com.gear.weathery.dashboard.models.WeatherResponse

class WeatherTimelineAdapter() : RecyclerView.Adapter<WeatherTimelineAdapter.TimelineViewHolder>() {
    private var weatherTimeline = listOf<WeatherResponse>()

    fun updateDataList(weatherTimeline : List<WeatherResponse> ){
        this.weatherTimeline = weatherTimeline
        notifyDataSetChanged()
    }


    class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var time = itemView.findViewById<TextView>(R.id.time_id)
        var weatherRisk = itemView.findViewById<TextView>(R.id.weather_risk_tv)
        var weatherDescription = itemView.findViewById<TextView>(R.id.weather_description_tv)
        var weatherIcon = itemView.findViewById<ImageView>(R.id.weather_icon)
        var ellipse = itemView.findViewById<ImageView>(R.id.ellipse_id)
        var dividerLine = itemView.findViewById<ImageView>(R.id.divider_line)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_list,parent,false)
        return TimelineViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val weatherTimelineItem = weatherTimeline[position]
        holder.time.setText(weatherTimelineItem.time)
        holder.weatherRisk.setText("None")
        holder.weatherDescription.setText(weatherTimelineItem.description)
        if(holder.adapterPosition == 0){
            holder.dividerLine.setImageResource(R.drawable.line_white)
            holder.ellipse.setImageResource(R.drawable.ellipse_white)
        }
        holder.weatherIcon.setImageResource(R.drawable.rainy_sharp)
    }


    override fun getItemCount(): Int {
       return weatherTimeline.size
    }

}