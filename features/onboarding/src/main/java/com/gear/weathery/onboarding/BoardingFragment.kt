package com.gear.weathery.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.navigation.DashBoardNavigation
import com.gear.weathery.onboarding.databinding.FragmentBoardingBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BoardingFragment : Fragment() {
    private var _binding:FragmentBoardingBinding? =  null
    private val binding get() = _binding!!

    @Inject
    lateinit var dashBoardNavigation: DashBoardNavigation
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.boardingText.text = arguments?.getString("args") ?:" boarding here"
        binding.boardingText.setOnClickListener {
            dashBoardNavigation.navigateToDashboard(navController = findNavController())
        }
    }

}