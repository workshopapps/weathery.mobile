package com.gear.weathery.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.preference.SettingsPreference
import com.gear.weathery.common.navigation.DashBoardNavigation
import com.gear.weathery.setting.databinding.FragmentSettingsBinding
import com.gear.weathery.setting.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
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
            lifecycleScope.launch {
                val defaultLanguage = Locale.getDefault().displayLanguage
                settingsPreference.currentLanguage().collect{ lang ->
                      val selectedLang =  Constants.languages.find { it.locale == lang }
                    tvLanguageSummary.text = if (selectedLang!=null) {
                        "${ selectedLang.lang }(${selectedLang.locale})"
                    }else{
                        "$defaultLanguage(United Kingdom)"
                    }
                }
            }
            radioGroup.setOnClickListener {  }

            tvSelectedPressureUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    TODO("Not yet implemented")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

            tvSelectedWindUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    TODO("Not yet implemented")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

        }

        return binding.root
    }
}