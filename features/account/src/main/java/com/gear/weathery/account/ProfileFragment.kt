package com.gear.weathery.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.navigation.DashBoardNavigation
import com.gear.weathery.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding:FragmentProfileBinding? =  null
    private val binding get() = _binding!!
    private lateinit var backPressedCallback: OnBackPressedCallback

    @Inject
    lateinit var dashBoardNavigation: DashBoardNavigation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            dashBoardNavigation.navigateToDashboard(navController = findNavController())
        }
        backPressedCallback.isEnabled = true
        _binding?.persolDataRel?.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToPersonalDataFragment())
        }
        _binding?.fab?.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToUploadPictureFragment());
        }

        _binding?.backToDashBoard?.setOnClickListener {
            dashBoardNavigation.navigateToDashboard(navController = findNavController())
        }
    }
}