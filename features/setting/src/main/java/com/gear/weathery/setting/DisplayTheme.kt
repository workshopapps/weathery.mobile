package com.gear.weathery.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.navigation.SharedPreference
import com.gear.weathery.common.preference.SettingsPreference
import com.gear.weathery.setting.databinding.FragmentDisplayThemeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DisplayTheme : Fragment() {
  private lateinit var binding: FragmentDisplayThemeBinding

  @Inject
  lateinit var settingsPreference: SettingsPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDisplayThemeBinding.inflate(inflater)
        binding.ivBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SharedPreference.init(requireContext())
        SharedPreference.putBoolean("THEMECHANGE",true)



        binding.apply {
            lifecycleScope.launch {
                settingsPreference.darkMode().collect{ theme ->
                    when(theme) {
                        "light".lowercase() -> {
                            rbtnLight.isChecked = true
                        }
                        "dark" -> {
                            rBtnDark.isChecked=true
                        }
                        "system" ->{
                            rbtnUseDeviceTheme.isChecked = true
                        }
                    }
                }
            }


            rgTheme.setOnCheckedChangeListener { _, checkedId ->
                val theme = SharedPreference.getBoolean("THEMECHANGE",true)

                when(checkedId){
                    R.id.rbtnLight ->{
                        lifecycleScope.launch {
                            settingsPreference.toggleDayMode("light")
                            requireActivity().recreate() }
                    }
                    R.id.rBtnDark ->{
                        lifecycleScope.launch {
                            settingsPreference.toggleDayMode("dark")
                            requireActivity().recreate()
                        }
                    }

                    R.id.rbtnUseDeviceTheme ->{
                        lifecycleScope.launch {
                            settingsPreference.toggleDayMode("system")
                            requireActivity().recreate()
                        }
                        }
                    }
                }
            }

        }
    }
