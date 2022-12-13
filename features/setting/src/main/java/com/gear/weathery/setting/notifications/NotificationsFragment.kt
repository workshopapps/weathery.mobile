package com.gear.weathery.setting.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.navigation.DashBoardNavigation
import com.gear.weathery.common.preference.SettingsPreference
import com.gear.weathery.location.api.LocationsRepository
import com.gear.weathery.setting.notifications.adapter.NotificationsAdapter
import com.gear.weathery.setting.databinding.FragmentNotificationsBinding
import com.gear.weathery.setting.notifications.database.NotificationDao
import com.gear.weathery.setting.notifications.model.NotificationData
import com.gear.weathery.setting.notifications.network.NetworkApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class Notifications : Fragment() {
    private lateinit var binding: FragmentNotificationsBinding
    private var adapter = NotificationsAdapter()

    @Inject
    lateinit var dashBoardNavigation: DashBoardNavigation

    @Inject
    lateinit var notificationDao: NotificationDao

    @Inject
    lateinit var locationsRepository: LocationsRepository

    @Inject
    lateinit var settingsPreference: SettingsPreference

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

    override fun onResume() {
        super.onResume()

        resetUnreadNotificationsCounter()
    }

    private fun resetUnreadNotificationsCounter() {
        lifecycleScope.launch {
            settingsPreference.updateUnreadNotificationCounter(0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerviewNotification.adapter = adapter
        binding.archiveButtonImageView.setOnClickListener {
            openBottomDialog()
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(
                    "NotificationsFragment",
                    "Fetching FCM registration token failed",
                    task.exception
                )
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("NotificationsToken", token)

        })

        notificationDao.getNotifications().onEach {
            adapter.updateDataList((it))
            if (it.isEmpty()) {
                binding.recyclerviewNotification.visibility = View.INVISIBLE
                binding.emptyStateGroup.visibility = View.VISIBLE
            } else {
                binding.recyclerviewNotification.visibility = View.VISIBLE
                binding.emptyStateGroup.visibility = View.INVISIBLE
            }
        }.launchIn(lifecycleScope)
    }

    private fun openBottomDialog() {
        val dialog = NotificationBottomSheetFragment()
        dialog.setCancelable(true)
        dialog.show(childFragmentManager, "NOTIFICATION SHEET")
    }
}