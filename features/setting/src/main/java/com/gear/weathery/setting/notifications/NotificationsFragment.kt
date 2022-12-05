package com.gear.weathery.setting.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.navigation.DashBoardNavigation
import com.gear.weathery.location.api.Location
import com.gear.weathery.location.api.LocationsRepository
import com.gear.weathery.setting.R
import com.gear.weathery.setting.notifications.adapter.NotificationsAdapter
import com.gear.weathery.setting.databinding.FragmentNotificationsBinding
import com.gear.weathery.setting.notifications.database.NotificationDao
import com.gear.weathery.setting.notifications.model.NotificationData
import com.gear.weathery.setting.notifications.network.NetworkApi
import com.gear.weathery.setting.notifications.network.NetworkService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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
    private lateinit var bottomSheetBehaviour: BottomSheetBehavior<ConstraintLayout>

    @Inject
    lateinit var dashBoardNavigation: DashBoardNavigation

    @Inject
    lateinit var notificationDao: NotificationDao

    @Inject
    lateinit var locationsRepository: LocationsRepository

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
        binding.recyclerviewNotification.adapter = adapter

        bottomSheetBehaviour = BottomSheetBehavior.from(view.findViewById<ConstraintLayout>(R.id.bottomSheet)).apply {
            peekHeight = 0
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.archiveButtonImageView.setOnClickListener {
            bottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("NotificationsFragment", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("NotificationsToken", token)

        })

        notificationDao.getNotifications().onEach {
            adapter.updateDataList((it))
            if (it.isEmpty() ){
                binding.recyclerviewNotification.visibility = View.INVISIBLE
                binding.emptyStateGroup.visibility = View.VISIBLE
            }else {
                binding.recyclerviewNotification.visibility = View.VISIBLE
                binding.emptyStateGroup.visibility = View.INVISIBLE
            }
        }.launchIn(lifecycleScope)

        lifecycleScope.launch {
            locationsRepository.locations.onEach { locations ->
                val currentLocation = locations.find {
                    it.name == "current location"
                }
                if (currentLocation != null){
                    FirebaseMessaging.getInstance().token.addOnCompleteListener{
                        lifecycleScope.launch {
                            if (it.isSuccessful){
                                val token = it.result
                                try {
                                    NetworkApi.retrofitService.subscribeNotifications(token,
                                        currentLocation.latitude, currentLocation.longitude )
                                } catch (e: Exception) {
                                    Log.d("Notification Error", e.message ?:"Unknown Error")
                                }
                            }
                        }
                    }
                }
            }.launchIn(lifecycleScope)
        }
        binding.frameEmptyNow.setOnClickListener {
            lifecycleScope.launch {
                notificationDao.deleteNotifications()
            }
        }
        val notificationList = mutableListOf<NotificationData>()
        notificationList.add(NotificationData(
            notificationText = "There will be heavy rainfall in some part in the east",
            notificationTime = "3m",
            notificationEvent = "Thunderstorm"
        ))
        notificationList.add(NotificationData(
            notificationText = "There will be heavy rainfall in some part in the east",
            notificationTime = "3m",
            notificationEvent = "Thunderstorm"
        ))
        notificationList.add(NotificationData(
            notificationText = "There will be heavy rainfall in some part in the east",
            notificationTime = "3m",
            notificationEvent = "Thunderstorm"
        ))
        notificationList.add(NotificationData(
            notificationText = "There will be heavy rainfall in some part in the east",
            notificationTime = "3m",
            notificationEvent = "Thunderstorm"
        ))
        lifecycleScope.launch {
            notificationDao.insert(*notificationList.toTypedArray())
        }


    }
}