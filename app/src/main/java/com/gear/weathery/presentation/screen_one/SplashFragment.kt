package com.gear.weathery.presentation.screen_one

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.navigation.BoardingNavigation
import com.gear.weathery.common.navigation.DashBoardNavigation
import com.gear.weathery.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding:FragmentSplashBinding? =  null
    private val binding get() = _binding!!

    @Inject
    lateinit var boardingNavigation: BoardingNavigation


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.onboardText.setOnClickListener {
            boardingNavigation.navigateToBoarding(navController = findNavController())
        }
    }

}