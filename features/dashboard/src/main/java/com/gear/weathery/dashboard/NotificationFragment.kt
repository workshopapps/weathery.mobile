package com.gear.weathery.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.gear.weathery.dashboard.models.NotificationData

class NotificationFragment : Fragment() {

    lateinit var notificationAdapter: NotificationAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var mockList = emptyList<NotificationData>()

        val emptyBtn = view.findViewById<FrameLayout>(R.id.emptyNowButton_frameLayout)

        mockList = listOf(
            NotificationData("There will be heavy rainfall from this morning through the night","2mins"),
            NotificationData("There will be excessive heat this afternoon","5mins"),
            NotificationData("Severe weather warning ","7mins"),
        )
        notificationAdapter = NotificationAdapter()
        notificationAdapter.updateDataList(dataList = mockList)

        emptyBtn.setOnClickListener {
            notificationAdapter.updateDataList(emptyList())
        }
    }

}