package com.gear.weathery.dashboard.ui

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.navigation.AddRemoveLocationNavigation
import com.gear.weathery.common.navigation.SettingsNavigation
import com.gear.weathery.common.navigation.SignInNavigation
import com.gear.weathery.dashboard.R
import com.gear.weathery.dashboard.databinding.FragmentDashBoardBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashBoardFragment : Fragment() , LocationListener{
    private val viewModel: DashBoardViewModel by activityViewModels {
        DashBoardViewModelFactory()
    }

    private val timeWeathersAdapter = TimeWeatherRecyclerAdapterX{
        viewModel.updateHighLightedTimeWeather(it)
    }

    private var _binding:FragmentDashBoardBinding? =  null
    private val binding get() = _binding!!
    private lateinit var backPressedCallback: OnBackPressedCallback

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    private lateinit var location: Location
    private var longitude : Int = 0
    private var latitude : Int = 0

    private lateinit var navDrawer: ConstraintLayout
    private lateinit var overlay: View

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

//        getLocation()
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

        navDrawer = binding.navDrawerConstraintLayout
        overlay = binding.overlayView

        _binding?.locationsMenuItemLinearLayout?.setOnClickListener{
            navigateToLocation()
        }

        _binding?.settingsMenuItemLinearLayout?.setOnClickListener {
            navigateToSettings()
        }

        _binding?.signInMenuItemLinearLayout?.setOnClickListener {
            navigateToSignin()
        }

        overlay.setOnClickListener {
            hideDialog(navDrawer)
        }

        binding.navLayoutButtonImageView.setOnClickListener{
            showDialog(navDrawer)
        }

        binding.weatherForTimesRecylcerView.adapter = timeWeathersAdapter

        viewModel.timeWeather.observe(viewLifecycleOwner){weathersList ->
            timeWeathersAdapter.updateItemList(weathersList)

            val highlightedWeather = weathersList.find { it.highlighted }!!
            binding.mainWidgetWeatherDescTextView.text = highlightedWeather.description
            binding.mainWidgetTimeRangeTextView.text = highlightedWeather.time
            val weatherIcon = when(highlightedWeather.main){
                "Clear" -> R.drawable.sun_orange
                "Clouds" -> R.drawable.rain_4
                else -> R.drawable.heavy_rain
            }
            binding.mainWidgetWeatherIconImageView.setImageResource(weatherIcon)
        }

        binding.shareButtonImageView.setOnClickListener{
            shareWeather()
        }

    }


    private fun navigateToSignin(){
        signInNavigation.navigateToSignIn(navController = findNavController())
    }

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

//    private fun generateMockTimesWeatherUIItems(): List<UITimesWeather>{
//        val uiTimesWeatherList = mutableListOf<UITimesWeather>()
//        val weatherNames = listOf("Rainy", "Cloudy", "Drizzle", "Fog", "Wind")
//        val weatherIconRsrcIds = listOf(R.drawable.rain, R.drawable.cloudy, R.drawable.drizzle, R.drawable.fog, R.drawable.wind)
//        for(item in 1..20){
//            val randomIndex = (0..4).random()
//            uiTimesWeatherList.add(UITimesWeather(weatherNames[randomIndex], weatherIconRsrcIds[randomIndex], "${item}am"))
//        }
//        return uiTimesWeatherList
//    }

    private fun exitApp() {
        if (exitAppToastStillShowing){
            requireActivity().finish()
            return
        }

        Toast.makeText(this.requireContext(), "press again to exit", Toast.LENGTH_SHORT).show()
        exitAppToastStillShowing = true
        exitAppTimer.start()
    }



    private fun getLocation() {
        locationManager =  (requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        latitude = location.latitude.toInt()
        longitude = location.longitude.toInt()


        this.location = location
        viewModel.updateLocation(location)

    }

    fun shareWeather(){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, viewModel.generateShareWeatherText())
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}