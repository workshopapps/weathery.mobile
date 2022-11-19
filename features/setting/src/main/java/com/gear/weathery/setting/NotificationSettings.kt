package com.gear.weathery.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gear.weathery.setting.databinding.FragmentNotificationSettingsBinding

class NotificationSettings : Fragment() {
    private lateinit var binding: FragmentNotificationSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationSettingsBinding.inflate(inflater)
        binding.ivBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

}