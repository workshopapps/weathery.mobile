package com.gear.weathery.dashboard.ui

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.gear.weathery.common.navigation.AddRemoveLocationNavigation
import com.gear.weathery.common.navigation.SettingsNavigation
import com.gear.weathery.common.navigation.SignInNavigation
import com.gear.weathery.dashboard.R
import com.gear.weathery.dashboard.databinding.FragmentDashBoardBinding
import com.gear.weathery.dashboard.models.UITimesWeather
import com.gear.weathery.dashboard.ui.viewPager.PagerCollectionAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashBoardFragment : Fragment() {
    private var _binding:FragmentDashBoardBinding? =  null
    private val binding get() = _binding!!
    private lateinit var backPressedCallback: OnBackPressedCallback

    private lateinit var demoCollectionAdapter: PagerCollectionAdapter
    private lateinit var viewPager: ViewPager2

    private lateinit var navDrawer: ConstraintLayout
    private lateinit var overlay: View
    private lateinit var scrollIndicator1: ImageView
    private lateinit var scrollIndicator2: ImageView
    private lateinit var scrollIndicator3: ImageView

    @Inject
    lateinit var settingsNavigation: SettingsNavigation

    @Inject
    lateinit var signInNavigation: SignInNavigation

    @Inject
    lateinit var locationsNavigation: AddRemoveLocationNavigation

    private var exitAppToastStillShowing = false

    private val exitAppTimer = object : CountDownTimer(2000, 1000) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            exitAppToastStillShowing = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            exitApp()
        }
        backPressedCallback.isEnabled = true
    }


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

//        scrollIndicator1 = binding.scrollIndicator1ImageView
//        scrollIndicator2 = binding.scrollIndicator2ImageView
//        scrollIndicator3 = binding.scrollIndicator3ImageView

        navDrawer = binding.navDrawerConstraintLayout
        overlay = binding.overlayView

        _binding?.locationsMenuItemLinearLayout?.setOnClickListener{
            navigateToLocation()
        }

        _binding?.settingsMenuItemLinearLayout?.setOnClickListener {
            navigateToSettings()
        }

//        _binding?.signInMenuItemLinearLayout?.setOnClickListener {
//            navigateToSignin()
//        }

        overlay.setOnClickListener {
            hideDialog(navDrawer)
        }

        binding.navLayoutButtonImageView.setOnClickListener{
            showDialog(navDrawer)
        }

        binding.weatherForTimesRecylcerView.adapter = TimesWeatherRecyclerAdapter().also { it.updateItemList(generateMockTimesWeatherUIItems()) }

        demoCollectionAdapter = PagerCollectionAdapter(this)
//        viewPager = view.findViewById(R.id.pager)
//        viewPager.adapter = demoCollectionAdapter
//        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                updateScrollIndicator(position)
//            }
//        })

    }
//
//    private fun updateScrollIndicator(newPosition: Int) {
//        when(newPosition){
//            0 -> {
//                scrollIndicator1.setImageResource(R.drawable.scoll_indicator_active)
//                scrollIndicator2.setImageResource(R.drawable.scoll_indicator_inactive)
//                scrollIndicator3.setImageResource(R.drawable.scoll_indicator_inactive)
//            }
//
//            1 -> {
//                scrollIndicator1.setImageResource(R.drawable.scoll_indicator_inactive)
//                scrollIndicator2.setImageResource(R.drawable.scoll_indicator_active)
//                scrollIndicator3.setImageResource(R.drawable.scoll_indicator_inactive)
//            }
//
//            2 -> {
//                scrollIndicator1.setImageResource(R.drawable.scoll_indicator_inactive)
//                scrollIndicator2.setImageResource(R.drawable.scoll_indicator_inactive)
//                scrollIndicator3.setImageResource(R.drawable.scoll_indicator_active)
//            }
//        }
//    }

//
//    private fun navigateToSignin(){
//        signInNavigation.navigateToSignIn(navController = findNavController())
//    }

    private fun navigateToSettings(){
        settingsNavigation.navigateToSettings(navController = findNavController())
    }


    private fun navigateToLocation(){
        locationsNavigation.navigateToAddRemoveLocation(navController = findNavController())
    }

    private fun hideDialog(dialog: ConstraintLayout) {
        overlay.visibility = View.GONE
        backPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            exitApp()
        }

        val movePropertyValueHolder = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f, -dialog.height.toFloat())
        val transparencyValueHolder = PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            dialog,
            movePropertyValueHolder,
            transparencyValueHolder
        )
        animator.start()

    }

    private fun showDialog(dialog: ConstraintLayout) {

        backPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            hideDialog(navDrawer)
        }

        overlay.visibility = View.VISIBLE
        navDrawer.visibility = View.VISIBLE

        val movePropertyValueHolder =
            PropertyValuesHolder.ofFloat(View.TRANSLATION_X, -dialog.width.toFloat(), 0f)
        val transparencyValueHolder = PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            dialog,
            movePropertyValueHolder,
            transparencyValueHolder
        )
        animator.start()
    }

    private fun generateMockTimesWeatherUIItems(): List<UITimesWeather>{
        val uiTimesWeatherList = mutableListOf<UITimesWeather>()
        val weatherNames = listOf(getString(R.string.rainy), getString(R.string.cloudy), getString(R.string.drizzle), getString(
                    R.string.fog), getString(R.string.wind))
        val weatherIconRsrcIds = listOf(R.drawable.rain, R.drawable.cloudy, R.drawable.drizzle, R.drawable.fog, R.drawable.wind)
        for(item in 1..20){
            val randomIndex = (0..4).random()
            uiTimesWeatherList.add(UITimesWeather(weatherNames[randomIndex], weatherIconRsrcIds[randomIndex], "${item}am"))
        }
        return uiTimesWeatherList
    }

    private fun exitApp() {
        if (exitAppToastStillShowing){
            requireActivity().finish()
            return
        }

        Toast.makeText(this.requireContext(), getString(R.string._exit_), Toast.LENGTH_SHORT).show()
        exitAppToastStillShowing = true
        exitAppTimer.start()
    }

}