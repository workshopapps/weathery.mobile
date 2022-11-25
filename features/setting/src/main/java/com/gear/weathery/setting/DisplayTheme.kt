package com.gear.weathery.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
        binding.apply {
            lifecycleScope.launchWhenCreated {
                settingsPreference.darkMode().collect{ theme ->
                    when(theme) {
                        getString(R.string.text_light) -> {
                            rBtnLight.isChecked = true
                        }
                        getString(R.string.text_dark) -> {
                            rBtnDark.isChecked=true
                        }
                        getString(R.string.text_system) ->{
                            rbtnUseDeviceTheme.isChecked = true
                        }
                    }
                }
            }


            rgTheme.setOnCheckedChangeListener { _, checkedId ->
                when(checkedId){
                    R.id.rBtnLight ->{
                        lifecycleScope.launch {
                            settingsPreference.toggleDayMode(getString(R.string.text_light))
                            requireActivity().recreate() }
                    }
                    R.id.rBtnDark ->{
                        lifecycleScope.launch {
                            settingsPreference.toggleDayMode(getString(R.string.text_dark))
                            requireActivity().recreate()
                        }
                    }

                    R.id.rBtnDark ->{
                        lifecycleScope.launch {
                            settingsPreference.toggleDayMode(getString(R.string.text_system))
                            requireActivity().recreate()
                        }
                        }
                    }
                }
            }

        }
    }
