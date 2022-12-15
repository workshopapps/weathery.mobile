package com.gear.weathery.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gear.weathery.dashboard.models.NotificationData

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    var dataList = listOf<NotificationData>()

    fun updateDataList(dataList : List<NotificationData> ){
        this.dataList = dataList
        notifyDataSetChanged()
    }



    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var notBody = itemView.findViewById<TextView>(R.id.notificationBody_textView)
        var notTime = itemView.findViewById<TextView>(R.id.age_textView)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notifications_list_item2,parent,false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = dataList[position]

        holder.notBody.setText(item.notificationText)
        holder.notTime.setText(item.notificationTime)

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}