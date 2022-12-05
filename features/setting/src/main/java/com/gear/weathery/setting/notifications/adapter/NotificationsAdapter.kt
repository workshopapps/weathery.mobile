package com.gear.weathery.setting.notifications.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gear.weathery.setting.databinding.NotificationsListItemBinding
import com.gear.weathery.setting.notifications.model.NotificationData

class NotificationsAdapter :  RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    var dataList = listOf<NotificationData>()

    fun updateDataList(dataList : List<NotificationData> ){
        this.dataList = dataList
        notifyDataSetChanged()
    }



    class NotificationViewHolder( val binding: NotificationsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (notificationData: NotificationData){
            binding.notificationBodyTextView.text=notificationData.notificationText
            binding.ageTextView.text=notificationData.notificationTime
//            binding.weatherSignal.image=notificationData.notificationEvent
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = NotificationsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = dataList[position]

        holder.bind(item)

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

//package com.gear.weathery.setting.notifications.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.gear.weathery.setting.databinding.NotificationsListItemBinding
//import com.gear.weathery.setting.notifications.model.NotificationData
//
//class NotificationsAdapter :  RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {
//
//    var dataList = listOf<NotificationData>()
//
//    fun updateDataList(dataList : List<NotificationData> ){
//        this.dataList = dataList
//        notifyDataSetChanged()
//    }
//
//
//
//    class NotificationViewHolder( val binding: NotificationsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind (notificationData: NotificationData){
//            binding.notificationBodyTextView.text=notificationData.notificationText
//            binding.ageTextView.text=notificationData.notificationTime
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
//        val view = NotificationsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return NotificationViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
//        val item = dataList[position]
//
//        holder.bind(item)
//
//    }
//
//    override fun getItemCount(): Int {
//        return dataList.size
//    }
//}