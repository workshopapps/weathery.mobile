package com.gear.weathery.setting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gear.weathery.setting.databinding.FragmentNotificationSettingsBinding

class NotificationSettings : Fragment() {
    private lateinit var binding: FragmentNotificationSettingsBinding
    private var isPushNotificationOn: Boolean = false
    private var vibrateOptionsExpanded: Boolean = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationSettingsBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            ivBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
            swNotificationOnOff.setOnCheckedChangeListener { buttonView, isChecked ->
                isPushNotificationOn = isChecked
                if (isChecked) {
                    rgPushNotifications.visibility = View.VISIBLE
                    rgVibrate.visibility = View.VISIBLE
                    swNotificationOnOff.text = "Turn off all notifications"
                } else {
                    rgPushNotifications.visibility = View.GONE
                    rgVibrate.visibility = View.GONE
                    swNotificationOnOff.text = "Turn on all notifications"
                }
            }
            tvVibrateStatus.setOnClickListener {
                Log.d("NotificationSetting", "Vibrate Status: $vibrateOptionsExpanded")
                if (vibrateOptionsExpanded) {
                    tvVibrateStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0, 0, R.drawable.ic_arrow_down, 0)
                    rgVibrate.visibility = View.VISIBLE
                    vibrateOptionsExpanded = false
                } else {
                    tvVibrateStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0, 0, R.drawable.ic_arrow_forward, 0)
                    rgVibrate.visibility = View.GONE
                    vibrateOptionsExpanded = true
                }
            }
            rgVibrate.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rBtnVibrateOff -> {
                        tvVibrateStatus.text = "Off"
                    }
                    R.id.rBtnVibrateDefault -> {
                        tvVibrateStatus.text = "Default"
                    }
                }
            }
            rgPushNotifications.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rBtnDaily -> {
                        rBtnDaily.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,0,R.drawable.ic_done,0)
                        rBtnMonthly.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0, 0, 0, 0)
                        rBtnWeekly.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0, 0, 0, 0)
                    }
                    R.id.rBtnWeekly -> {
                        rBtnWeekly.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0, 0, R.drawable.ic_done, 0)
                        rBtnDaily.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,0,0,0)
                        rBtnMonthly.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0, 0, 0, 0)
                    }
                    R.id.rBtnMonthly -> {
                        rBtnMonthly.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0, 0, R.drawable.ic_done, 0)
                        rBtnWeekly.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0, 0, 0, 0)
                        rBtnDaily.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,0,0,0)
                    }
                }
            }


        }
    }

}