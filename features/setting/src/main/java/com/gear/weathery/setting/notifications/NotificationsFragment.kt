package com.gear.weathery.setting.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.navigation.DashBoardNavigation
import com.gear.weathery.setting.adapters.NotificationsAdapter
import com.gear.weathery.setting.databinding.FragmentNotificationsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Notifications : Fragment() {
    private lateinit var binding: FragmentNotificationsBinding

    private var adapter = NotificationsAdapter()

    @Inject
    lateinit var dashBoardNavigation: DashBoardNavigation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsBinding.inflate(inflater)
        binding.backButtonImageView.setOnClickListener {
            dashBoardNavigation.navigateToDashboard(navController = findNavController())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.notif.adapter = adapter

        val notificationlist = listOf(
            NotificationData("There will be heavy rain for some in northeast with a few brighter exceptions.", "12m"),
            NotificationData("There will be heavy rain for some in northeast with a few brighter exceptions.", "12m"),
            NotificationData("There will be heavy rain for some in northeast with a few brighter exceptions.", "12m"),
            NotificationData("There will be heavy rain for some in northeast with a few brighter exceptions.", "12m"),
            NotificationData("There will be heavy rain for some in northeast with a few brighter exceptions.", "12m"),
            NotificationData("There will be heavy rain for some in northeast with a few brighter exceptions.", "12m")
        )
        adapter.updateDataList(notificationlist)
        binding.emptyNowButtonFrameLayout.setOnClickListener {
            adapter.updateDataList(emptyList())
        }
    }
}