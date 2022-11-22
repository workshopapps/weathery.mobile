package com.gear.weathery.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gear.weathery.setting.adapters.LanguagesAdapter
import com.gear.weathery.setting.databinding.FragmentSelectLanguageBinding
import com.gear.weathery.setting.util.Constants

class SelectLanguage : Fragment() {
  private lateinit var binding: FragmentSelectLanguageBinding
  private lateinit var languagesAdapter: LanguagesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectLanguageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        languagesAdapter = LanguagesAdapter()
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