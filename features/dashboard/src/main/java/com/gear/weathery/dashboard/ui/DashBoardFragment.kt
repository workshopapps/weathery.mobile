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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.navigation.*
import com.gear.weathery.dashboard.databinding.FragmentDashBoardBinding
import com.gear.weathery.dashboard.models.DayWeather
import com.gear.weathery.dashboard.models.getTimeForDisplay
import com.gear.weathery.dashboard.network.URL_TO_SHARE
import com.gear.weathery.dashboard.ui.DashboardViewModel.DashboardViewModelFactory
import com.gear.weathery.dashboard.ui.DashboardViewModel.ShareLinkEvents
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DashBoardFragment : Fragment(), LocationListener {
    private lateinit var binding: FragmentDashBoardBinding
    private val viewModel: DashboardViewModel by activityViewModels { DashboardViewModelFactory() }
    private val adapter = TimelineRecyclerAdapter()

    private lateinit var backPressedCallback: OnBackPressedCallback
   // private lateinit var timelineAdapter: WeatherTimelineAdapter

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    private var longitude: Int = 0
    private var latitude: Int = 0
    private lateinit var currentLocation: Location

    private lateinit var navDrawer: ConstraintLayout
    private lateinit var overlay: View

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @Inject
    lateinit var settingsNavigation: SettingsNavigation

    @Inject
    lateinit var notificationsNavigation: NotificationsNavigation

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        // this checks if the required permissions have been granted.
        // this check is redundant here since the permission was already granted in the previous screen before navigating here.
        // but without this check here again, android studio keeps showing errors. so,...
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener { location: Location? ->
            viewModel.updateWeatherWithNewLocation(location)
            location?.let {
                currentLocation = it
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashBoardBinding.inflate(inflater, container, false)

        binding.apply {
            binding.timelineRecyclerView.adapter = adapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        scrollIndicator1 = binding.scrollIndicator1ImageView
//        scrollIndicator2 = binding.scrollIndicator2ImageView
//        scrollIndicator3 = binding.scrollIndicator3ImageView\

        //timelineAdapter = WeatherTimelineAdapter()
        //timelineAdapter.updateDataList()
       // binding.timelineRecyclerView.adapter = timelineAdapter

        SharedPreference.init(requireContext())
        var permissionAllowed = SharedPreference.getBoolean("ALLOWPERMISSION", true)


        navDrawer = binding.navDrawerConstraintLayout
        overlay = binding.overlayView

        binding.locationsMenuItemLinearLayout.setOnClickListener {
            navigateToLocation()
        }
        binding.notificationsMenuItemLinearLayout.setOnClickListener {
            navigateToNotifications()
        }

        binding.settingsMenuItemLinearLayout.setOnClickListener {
            navigateToSettings()
        }

        binding.signInMenuItemLinearLayout.setOnClickListener {
            navigateToSignin()
        }

        overlay.setOnClickListener {
            hideDialog(navDrawer)
        }

        binding.navLayoutButtonImageView.setOnClickListener {
            showDialog(navDrawer)
        }

        viewModel.dayWeather.observe(viewLifecycleOwner) {
            updateViews(it)
        }

        binding.shareButtonImageView.setOnClickListener {
            updateWeatherLink()
        }

    }


    private fun updateViews(it: DayWeather?) {
        if (it == null) {
            return
        }

        binding.stateAndCountryTextView.text = "${it.state}, ${it.country}"
        binding.currentWeatherTextView.text = it.currentWeather.main
        val startTime = getTimeForDisplay(it.currentWeather.timeInMillis)
        val endTime = getTimeForDisplay(it.currentWeather.endTimeTimeInMillis)
        binding.currentWeatherTimeTextView.text = "$startTime to $endTime"
        binding.currentWeatherRiskTextView.text = it.currentWeather.risk
        adapter.updateItemList(it.timeLine)
    }


    private fun navigateToSignin() {
        signInNavigation.navigateToSignIn(navController = findNavController())
    }

    private fun navigateToSettings() {
        settingsNavigation.navigateToSettings(navController = findNavController())
    }

    private fun navigateToNotifications() {
        notificationsNavigation.navigateToNotifications(navController = findNavController())
    }

    private fun navigateToLocation() {
        locationsNavigation.navigateToAddRemoveLocation(navController = findNavController())
    }

    private fun hideDialog(dialog: ConstraintLayout) {
        overlay.visibility = View.GONE
        backPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            exitApp()
        }

        val movePropertyValueHolder =
            PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f, -dialog.height.toFloat())
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

    private fun exitApp() {
        if (exitAppToastStillShowing) {
            requireActivity().finish()
            return
        }

        Toast.makeText(this.requireContext(), "press again to exit", Toast.LENGTH_SHORT).show()
        exitAppToastStillShowing = true
        exitAppTimer.start()
    }

    private fun updateWeatherLink() {
        viewModel.getSharedWeatherLink(currentLocation.latitude, currentLocation.longitude)
        lifecycleScope.launch {
            viewModel.sharedLinkEvent.collect { linkResponse ->
                when (linkResponse) {
                    is ShareLinkEvents.Successful -> {

                        linkResponse.data.data?.link?.let {
                            getShareIntent(it)
                        } ?: Toast.makeText(
                            requireContext(),
                            "Current Location Link Not Available",
                            Toast.LENGTH_SHORT
                        ).show()
                        getShareIntent(URL_TO_SHARE)
                    }
                    is ShareLinkEvents.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            "Current Location Link Not Available",
                            Toast.LENGTH_SHORT
                        ).show()
                        getShareIntent(URL_TO_SHARE)
                    }
                }
            }
        }
    }

    private fun getShareIntent(url: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(Intent.createChooser(intent, "Share"))
    }


    private fun getLocation() {
        locationManager =
            (requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        latitude = location.latitude.toInt()
        longitude = location.longitude.toInt()

    }

}