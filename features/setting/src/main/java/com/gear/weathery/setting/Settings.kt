package com.gear.weathery.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
//import com.gear.weathery.common.preference.SettingsPreference
import com.gear.weathery.common.navigation.DashBoardNavigation
import com.gear.weathery.setting.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Settings : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    @Inject
    lateinit var dashBoardNavigation: DashBoardNavigation

    @Inject
    lateinit var settingsPreference: SettingsPreference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleScope.launchWhenStarted {
                settingsPreference.pushNotification().collect{ isPushNotification ->
                    tvNotificationStatus.text = if (isPushNotification){
                        getString(R.string.on)
                    }else{
                        getString(R.string.off)
                    }
                }
            }

            ivBackButton.setOnClickListener {
                dashBoardNavigation.navigateToDashboard(navController = findNavController())
            }

            IvNotificationBtn.setOnClickListener {
                findNavController().navigate(R.id.notificationSettings)
            }
            ivLanguagebtn.setOnClickListener {
                findNavController().navigate(R.id.selectLanguage)
            }
            ivThemesBtn.setOnClickListener {
                findNavController().navigate(R.id.displayTheme)
            }
        }
//        binding.apply {
//            lifecycleScope.launchWhenStarted {
//                settingsPreference.pushNotification().collect{ isPushNotification ->
//                    tvNotificationStatus.text = if (isPushNotification){
//                        getString(R.string.on_label)
//                    }else{
//                        getString(R.string.off_label)
//                    }
//                }
//            }
//
//            ivBackButton.setOnClickListener {
//                dashBoardNavigation.navigateToDashboard(navController = findNavController())
//            }
//
//            IvNotificationBtn.setOnClickListener {
//                findNavController().navigate(R.id.notificationSettings)
//            }
//            ivLanguagebtn.setOnClickListener {
//                findNavController().navigate(R.id.selectLanguage)
//            }
//            ivThemesBtn.setOnClickListener {
//                findNavController().navigate(R.id.displayTheme)
//            }
//        }

        return binding.root
    }
}