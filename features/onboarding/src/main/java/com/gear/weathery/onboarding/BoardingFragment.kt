package com.gear.weathery.onboarding

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.gear.weathery.common.navigation.DashBoardNavigation
import com.gear.weathery.onboarding.databinding.FragmentBoardingBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BoardingFragment : Fragment(), PermissionListener {
    private var _binding:FragmentBoardingBinding? =  null
    private val binding get() = _binding!!
    lateinit var viewPager: ViewPager
    lateinit var pagerAdapter: ViewPagerAdapter

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
        pagerAdapter = ViewPagerAdapter(requireContext())
        binding.viewpager.adapter = pagerAdapter
        binding.dotsIndicator.attachTo(viewPager)
        binding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {


                if(position == 0){
                    onboardFirstPageBtnDesign()
                    binding.contBtn.setOnClickListener {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    }
                    binding.skipBtn.setOnClickListener {
                        viewPager.setCurrentItem(viewPager.getAdapter()!!.getCount());
                    }
                }

                if (position == 1){
                    onboardFirstPageBtnDesign()
                    binding.contBtn.setOnClickListener {
                        if (viewPager.currentItem < viewPager.adapter!!.count)
                            viewPager.setCurrentItem(viewPager.currentItem + 1);
                    }
                    binding.skipBtn.setOnClickListener {
                        viewPager.setCurrentItem(viewPager.adapter!!.count);
                    }
                }

                if (position==2){
                    onboardSecondPageBtnDesign()
                    binding.skipBtn.setOnClickListener {
                        getLocationPermission()
                    }
                }


            }

            override fun onPageSelected(position: Int) {
               // TODO("Not yet implemented")
            }

            override fun onPageScrollStateChanged(state: Int) {
               // TODO("Not yet implemented")
            }

        })


    }

    private fun getLocationPermission() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(this@BoardingFragment)
            .check()
    }

    private fun onboardSecondPageBtnDesign() {
        binding.contBtn.visibility = View.GONE
        binding.skipBtn.setBackgroundResource(R.drawable.onboard_button_round_corner)
        binding.skipBtn.setText("Get Started")
        binding.skipBtn.setTextColor(Color.WHITE)
    }

    private fun onboardFirstPageBtnDesign() {
        binding.contBtn.visibility = View.VISIBLE
        binding.skipBtn.visibility = View.VISIBLE
        binding.contBtn.setBackgroundResource(R.drawable.onboard_button_round_corner)
        binding.contBtn.setText("Continue")
        binding.contBtn.setTextColor(Color.WHITE)
        binding.skipBtn.setTextColor(resources.getColor(R.color.dark_orange))
        binding.skipBtn.setText("Skip")
        binding.skipBtn.setBackgroundResource(R.drawable.onboard_transparent_btn_bg)
    }


    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
        SharedPreference.init(requireContext().applicationContext)
        SharedPreference.putBoolean("ALLOW",false)
    }

    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
       // TODO("Not yet implemented")
    }

    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
       // TODO("Not yet implemented")
    }

}