package com.gear.weathery.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.navigation.SplashNavigation
import com.gear.weathery.dashboard.databinding.FragmentDashBoardBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashBoardFragment : Fragment() {
    private var _binding:FragmentDashBoardBinding? =  null
    private val binding get() = _binding!!

    @Inject
    lateinit var splashNavigation: SplashNavigation
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dashBoardText.setOnClickListener {
            splashNavigation.navigateToSplash(navController = findNavController())
        }
    }


}