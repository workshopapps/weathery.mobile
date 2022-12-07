package com.gear.weathery.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gear.weathery.common.navigation.SharedPreference
import com.gear.weathery.common.preference.SettingsPreference
import com.gear.weathery.setting.adapters.LanguagesAdapter
import com.gear.weathery.setting.databinding.FragmentSelectLanguageBinding
import com.gear.weathery.setting.util.Constants
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SelectLanguage : Fragment() {
  private lateinit var binding: FragmentSelectLanguageBinding
  private lateinit var languagesAdapter: LanguagesAdapter
  var isCurrentLang:Boolean = false

    @Inject
    lateinit var settingsPreference: SettingsPreference
    private val settingsViewModel:SettingsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectLanguageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SharedPreference.init(requireContext())
        SharedPreference.putBoolean("CHANGELANGUAGE",true)
        languagesAdapter = LanguagesAdapter{ selectedLanguage ->
            Toast.makeText(requireContext(), "${selectedLanguage.lang}", Toast.LENGTH_SHORT).show()
            settingsViewModel.saveLanguage(selectedLanguage.locale)
            Lingver.getInstance().setLocale(requireContext(), selectedLanguage.locale)
            recreate(requireActivity())

        }
        lifecycleScope.launch{
            settingsPreference.currentLanguage().collect{langValue ->
                Constants.languages.forEach {
                    if (it.locale == langValue) {
                        it.isSelected = true
                    } else {
                        it.isSelected = false
                    }
                }
            }
        }
        languagesAdapter.submitList(Constants.languages)
        binding.apply {
            ivBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
            rvLanguages.apply {
                adapter = languagesAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }

        }

    }

}