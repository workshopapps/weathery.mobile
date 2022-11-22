package com.gear.weathery.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.navigation.SettingsNavigation
import com.gear.weathery.setting.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Settings : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var settingsNavigation: SettingsNavigation
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            ivBackButton.setOnClickListener {
                settingsNavigation.navigateToSettings(navController = findNavController())
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
    }
}