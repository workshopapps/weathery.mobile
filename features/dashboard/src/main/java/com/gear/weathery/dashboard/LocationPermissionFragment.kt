package com.gear.weathery.dashboard

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gear.weathery.common.navigation.SharedPreference
import com.gear.weathery.dashboard.databinding.FragmentLocationPermissionBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationPermissionFragment : BottomSheetDialogFragment() {

    private lateinit var client: FusedLocationProviderClient

    private lateinit var binding : FragmentLocationPermissionBinding
    private val locationPermissionCode = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLocationPermissionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.allowPermission.setOnClickListener {
            startActivity(Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", requireContext().packageName, null)
                dismiss()
                SharedPreference.init(requireContext())
                SharedPreference.putBoolean("ALLOWPERMISSION",true)
            })
        }

    }

}