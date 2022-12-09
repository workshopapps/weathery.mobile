package com.gear.weathery.setting.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.gear.weathery.setting.databinding.FragmentNotificationBottomSheetBinding
import com.gear.weathery.setting.notifications.database.NotificationDao
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBottomSheetBinding

    @Inject
    lateinit var notificationDao: NotificationDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.frameEmptyNow.setOnClickListener {
            lifecycleScope.launch {
                notificationDao.deleteNotifications()
                dismiss()
            }

        }
        binding.frameCancel.setOnClickListener {
            dismiss()
        }
    }
}