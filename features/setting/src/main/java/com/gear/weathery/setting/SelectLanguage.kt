package com.gear.weathery.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gear.weathery.setting.databinding.FragmentSelectLanguageBinding

class SelectLanguage : Fragment() {
  private lateinit var binding: FragmentSelectLanguageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectLanguageBinding.inflate(inflater)
        binding.ivBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

}