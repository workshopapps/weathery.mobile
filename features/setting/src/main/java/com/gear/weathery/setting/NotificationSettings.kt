package com.gear.weathery.setting

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.preference.SettingsPreference
import com.gear.weathery.setting.databinding.FragmentNotificationSettingsBinding

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class NotificationSettings : Fragment() {
    private lateinit var binding: FragmentNotificationSettingsBinding
    private var isPushNotificationOn: Boolean = false
    private var vibrateOptionsExpanded: Boolean = true
    @Inject
    lateinit var settingsPreference: SettingsPreference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationSettingsBinding.inflate(inflater)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleScope.launch {
                settingsPreference.pushNotification().collect{ isPushNotification ->
                        isPushNotificationOn = isPushNotification
                        swNotificationOnOff.isChecked = isPushNotification
                        swNotificationOnOff.text = if (isPushNotification){
                            rgPushNotifications.visibility = View.VISIBLE
                            getString(R.string.turn_off_all_notifications)
                        }else{
                            rgPushNotifications.visibility = View.GONE
                            getString(R.string.turn_on_all_notifications)
                        }
                    }
            }
            lifecycleScope.launch {
                settingsPreference.vibrateMode().collect{ isVibrateDefault ->
                        if (isVibrateDefault){
                            rBtnVibrateDefault.isChecked = true
                            tvVibrateStatus.text = getString(R.string.default_fate)
                        }else{
                            rBtnVibrateOff.isChecked = true
                            tvVibrateStatus.text = getString(R.string.off)
                        }

                    }}
            lifecycleScope.launch {
                settingsPreference.pushNotificationFrequency().collect{ frequency ->
                        when (frequency) {
                            getString(R.string.daily).lowercase() -> {
                                rBtnDaily.isChecked = true
                                rBtnDaily.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                    0,0,R.drawable.ic_done,0)
                                rBtnMonthly.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                    0, 0, 0, 0)
                                rBtnWeekly.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                    0, 0, 0, 0)
                            }
                            getString(R.string.weekly).lowercase() -> {
                                rBtnWeekly.isChecked = true
                                rBtnWeekly.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                    0, 0, R.drawable.ic_done, 0)
                                rBtnDaily.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                    0,0,0,0)
                                rBtnMonthly.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                    0, 0, 0, 0)
                            }
                            getString(R.string.monthly).lowercase() -> {
                                rBtnMonthly.isChecked = true
                                rBtnMonthly.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                    0, 0, R.drawable.ic_done, 0)
                                rBtnWeekly.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                    0, 0, 0, 0)
                                rBtnDaily.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                    0,0,0,0)
                            }
                        }
                    }}
            lifecycleScope.launch {
                settingsPreference.bannerEnabled().collect { isBannerEnabled ->
                    swBannerOnOff.isChecked = isBannerEnabled
                }
            }
            lifecycleScope.launch {
                settingsPreference.notificationTone().collect{ tone ->
                    tvDefaultTone.text = tone?:"Default(fate)"
                }

            }



            ivBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
            swNotificationOnOff.setOnCheckedChangeListener { _, isChecked ->
                isPushNotificationOn = isChecked
                if (isChecked) {
                    lifecycleScope.launch {
                        settingsPreference.togglePushNotification(true)
                    }
                } else {
                    lifecycleScope.launch {
                        settingsPreference.togglePushNotification(false)
                    }
                }
            }
            tvVibrateStatus.setOnClickListener {
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
            tvDefaultTone.setOnClickListener {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, "channel.id" )
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName )
                startActivity(intent)
            }
            rgVibrate.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rBtnVibrateOff -> {
                        tvVibrateStatus.text = getString(R.string.off)
                        lifecycleScope.launch {
                            settingsPreference.toggleVibrationMode(false)
                        }
                    }
                    R.id.rBtnVibrateDefault -> {
                        tvVibrateStatus.text = getString(R.string.default1)
                        lifecycleScope.launch {
                            settingsPreference.toggleVibrationMode(true)
                        }

                    }
                }
            }
            rgPushNotifications.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rBtnDaily -> {
                        lifecycleScope.launch {
                            settingsPreference.setPushNotification(frequency = getString(R.string.daily).lowercase())
                        }
                    }
                    R.id.rBtnWeekly -> {
                        lifecycleScope.launch {
                            settingsPreference.setPushNotification(frequency = getString(R.string.weekly).lowercase())
                        }
                    }
                    R.id.rBtnMonthly -> {
                        lifecycleScope.launch {
                            settingsPreference.setPushNotification(frequency = getString(R.string.monthly).lowercase())
                        }
                    }
                }
            }

            swBannerOnOff.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked){
                    lifecycleScope.launch {
                        settingsPreference.toggleBanner(true)
                    }
                }else{
                    lifecycleScope.launch {
                        settingsPreference.toggleBanner( false)
                    }
                }
            }
        }
    }

}