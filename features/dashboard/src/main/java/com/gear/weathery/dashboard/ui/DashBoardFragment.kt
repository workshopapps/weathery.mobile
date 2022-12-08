package com.gear.weathery.dashboard.ui

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.navigation.*
import com.gear.weathery.dashboard.R
import com.gear.weathery.dashboard.databinding.FragmentDashBoardBinding
import com.gear.weathery.dashboard.models.TimelineWeather
import com.gear.weathery.dashboard.models.WeatherCondition
import com.gear.weathery.dashboard.models.getTimeForDisplay
import com.gear.weathery.dashboard.network.URL_TO_SHARE
import com.gear.weathery.dashboard.repository.NONE
import com.gear.weathery.dashboard.ui.DashboardViewModel.DashboardViewModelFactory
import com.gear.weathery.dashboard.ui.DashboardViewModel.ShareLinkEvents
import com.gear.weathery.dashboard.util.OnClickEvent
import com.gear.weathery.location.api.LocationsRepository
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

const val REQUEST_LOCATION_SETTINGS = 25


@AndroidEntryPoint
class DashBoardFragment : Fragment(), LocationListener, OnClickEvent {
    private lateinit var binding: FragmentDashBoardBinding

    @Inject
    lateinit var locationsRepository: LocationsRepository
    private val viewModel: DashboardViewModel by activityViewModels {
        DashboardViewModelFactory(
            locationsRepository
        )
    }
    private val adapter = TimelineRecyclerAdapter()

    private lateinit var backPressedCallback: OnBackPressedCallback

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    private var longitude: Int = 0
    private var latitude: Int = 0
    private lateinit var currentLocation: Location

    private lateinit var navDrawer: ConstraintLayout
    private lateinit var overlay: View
    private lateinit var viewsMenu: LinearLayout

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

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

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    viewModel.updateCurrentLocation(location)
                    lifecycleScope.launch {
                        val savedLocation = com.gear.weathery.location.api.Location(id = 1, state = "",
                            name = "current location", country = "", longitude = location.longitude,
                            latitude = location.latitude
                        )
                        locationsRepository.saveLocation(savedLocation)
                    }
                }
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
      //  setUpLocation()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    private fun startLocationUpdates() {

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

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun setUpLocation() {

        locationRequest = (LocationRequest.create().apply {
            interval = 2 * 60 * 1000
            fastestInterval = 60 * 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        })

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...

            startGettingLocationUpdates()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(requireActivity(), REQUEST_LOCATION_SETTINGS)

                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }

    }

    private fun startGettingLocationUpdates() {

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
        /// end of check


        Handler().postDelayed({
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                viewModel.updateCurrentLocation(location)
                currentLocation = location!!
            }
        }, 200)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_LOCATION_SETTINGS) {
            startGettingLocationUpdates()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashBoardBinding.inflate(inflater, container, false)
        setUpLocation()
        binding.apply {
            binding.timelineRecyclerView.adapter = adapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.locationHeaderLinearLayout.setOnClickListener {
            BottomSheetDrawer().show(childFragmentManager, "BOTTOM SHEET")
        }

        SharedPreference.init(requireContext())
        var permissionAllowed = SharedPreference.getBoolean("ALLOWPERMISSION", true)


        navDrawer = binding.navDrawerConstraintLayout
        overlay = binding.overlayView
        viewsMenu = binding.timelineViewsMenuLinearLayout

        binding.locationsMenuItemLinearLayout.setOnClickListener {
            navigateToLocation()
        }
        binding.notificationsMenuItemLinearLayout.setOnClickListener {
            navigateToNotifications()
        }

        binding.settingsMenuItemLinearLayout.setOnClickListener {
            navigateToSettings()
        }



        binding.navLayoutButtonImageView.setOnClickListener {
            showDialog(navDrawer)
        }


        viewModel.currentWeather.observe(viewLifecycleOwner) {
            updateViewsForNewCurrentWeather(it)
        }

        viewModel.timeline.observe(viewLifecycleOwner) {
            updateViewsForNewTimeline(it)
        }

        binding.shareButtonImageView.setOnClickListener {
            updateWeatherLink()
        }

        viewModel.viewMode.observe(viewLifecycleOwner) {
            updateViewsForNewViewMode(it)
        }

        binding.timelineViewsMenuImageView.setOnClickListener {
            showViewsMenu(viewsMenu)
        }

        binding.todayGroupLinearLayout.setOnClickListener {
            setTodayView()
        }

        binding.tomorrowGroupLinearLayout.setOnClickListener {
            setTomorrowView()
        }

        binding.thisWeekGroupLinearLayout.setOnClickListener {
            setThisWeekView()
        }

        viewModel.currentWeatherStatus.observe(viewLifecycleOwner) {
            updateViewsForNewCurrentWeatherStatus(it)
        }

        viewModel.timelineStatus.observe(viewLifecycleOwner) {
            updateViewsForNewTimelineStatus(it)
        }

    }


    private fun updateViewsForNewCurrentWeatherStatus(newCurrentWeatherStatus: Int?) {
        if (newCurrentWeatherStatus == null) {
            return
        }

        when (newCurrentWeatherStatus) {

            BUSY -> {
                binding.currentWeatherLoadingTextView.visibility = View.VISIBLE
                binding.currentWeatherGroupLinearLayout.visibility = View.INVISIBLE
                binding.currentWeatherDefaultTextView.visibility = View.GONE
                binding.currentWeatherErrorTextView.visibility = View.GONE
            }

            PASSED -> {
                binding.currentWeatherLoadingTextView.visibility = View.GONE
                binding.currentWeatherGroupLinearLayout.visibility = View.VISIBLE
                binding.currentWeatherDefaultTextView.visibility = View.GONE
                binding.currentWeatherErrorTextView.visibility = View.GONE
            }

            FAILED -> {
                binding.currentWeatherLoadingTextView.visibility = View.GONE
                binding.currentWeatherGroupLinearLayout.visibility = View.INVISIBLE
                binding.currentWeatherDefaultTextView.visibility = View.GONE
                binding.currentWeatherErrorTextView.visibility = View.VISIBLE
            }

            DEFAULT -> {
                binding.currentWeatherLoadingTextView.visibility = View.GONE
                binding.currentWeatherGroupLinearLayout.visibility = View.INVISIBLE
                binding.currentWeatherDefaultTextView.visibility = View.VISIBLE
                binding.currentWeatherErrorTextView.visibility = View.GONE
            }
        }
    }

    private fun updateViewsForNewTimelineStatus(newTimelineStatus: Int?) {
        if (newTimelineStatus == null) {
            return
        }

        when (newTimelineStatus) {
            BUSY -> {
                binding.timelineLoadingTextView.visibility = View.VISIBLE
                binding.timelineRecyclerView.visibility = View.GONE
                binding.timelineErrorTextView.visibility = View.GONE
                binding.timelineDefaultTextView.visibility = View.GONE
            }

            PASSED -> {
                binding.timelineLoadingTextView.visibility = View.GONE
                binding.timelineRecyclerView.visibility = View.VISIBLE
                binding.timelineErrorTextView.visibility = View.GONE
                binding.timelineDefaultTextView.visibility = View.GONE
            }

            FAILED -> {
                binding.timelineLoadingTextView.visibility = View.GONE
                binding.timelineRecyclerView.visibility = View.GONE
                binding.timelineErrorTextView.visibility = View.VISIBLE
                binding.timelineDefaultTextView.visibility = View.GONE
            }

            DEFAULT -> {
                binding.timelineLoadingTextView.visibility = View.GONE
                binding.timelineRecyclerView.visibility = View.GONE
                binding.timelineErrorTextView.visibility = View.GONE
                binding.timelineDefaultTextView.visibility = View.VISIBLE
            }
        }
    }

    private fun updateViewsForNewViewMode(
        newViewMode: String?
    ) {
        if (newViewMode == null) {
            return
        }

        when (newViewMode) {
            TODAY_VIEW_MODE -> {
                binding.apply {
                    currentViewTextView.text = "Today"
                    todayTextView.setTextColor(resources.getColor(R.color.weathery_orange))
                    todayIconImageView.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.weathery_orange
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    tomorrowTextView.setTextColor(resources.getColor(R.color.default_colour))
                    tomorrowIconImageView.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.default_colour
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    thisWeekTextView.setTextColor(resources.getColor(R.color.default_colour))
                    thisWeekIconImageView.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.default_colour
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                }
            }

            TOMORROW_VIEW_MODE -> {
                binding.apply {
                    currentViewTextView.text = "Tomorrow"
                    tomorrowTextView.setTextColor(resources.getColor(R.color.weathery_orange))
                    tomorrowIconImageView.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.weathery_orange
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    thisWeekTextView.setTextColor(resources.getColor(R.color.default_colour))
                    thisWeekIconImageView.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.default_colour
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    todayTextView.setTextColor(resources.getColor(R.color.default_colour))
                    todayIconImageView.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.default_colour
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                }
            }

            THIS_WEEK_VIEW_MODE -> {
                binding.apply {
                    currentViewTextView.text = "This Week"
                    thisWeekTextView.setTextColor(resources.getColor(R.color.weathery_orange))
                    thisWeekIconImageView.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.weathery_orange
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    tomorrowTextView.setTextColor(resources.getColor(R.color.default_colour))
                    tomorrowIconImageView.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.default_colour
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    todayTextView.setTextColor(resources.getColor(R.color.default_colour))
                    todayIconImageView.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.default_colour
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                }
            }

        }
    }


    private fun updateViewsForNewCurrentWeather(
        newCurrentWeather: WeatherCondition?
    ) {
        if (newCurrentWeather == null) {
            return
        }

        binding.stateAndCountryTextView.text =
            "${newCurrentWeather.state}, ${newCurrentWeather.country}"
        binding.currentWeatherTextView.text = newCurrentWeather.main
        val startTime = getTimeForDisplay(newCurrentWeather.timeInMillis)
        val endTime = getTimeForDisplay(newCurrentWeather.endTimeTimeInMillis)
        binding.currentWeatherTimeTextView.text = "$startTime to $endTime"
        binding.currentWeatherRiskTextView.text = newCurrentWeather.risk
        binding.locationTextView.text =
            "${newCurrentWeather.state}, ${newCurrentWeather.country}"
        binding.currentWeatherRiskIndicatorImageView.setImageResource(if(newCurrentWeather.risk == NONE) R.drawable.ic_warning_inactive else R.drawable.ic_warning_active)

    }

    private fun updateViewsForNewTimeline(
        newTimeLine: Pair<List<TimelineWeather>, String>
    ) {
        adapter.updateItemList(newTimeLine.first, newTimeLine.second)
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

    private fun hideDialog(dialog: View) {
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

    private fun hideViewsMenu(
        dialog: View = viewsMenu
    ) {
        overlay.visibility = View.GONE
        backPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            exitApp()
        }

        val transparencyValueHolder = PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            dialog,
            transparencyValueHolder
        )
        animator.start()

    }

    private fun showDialog(dialog: View) {

        backPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            hideDialog(dialog)
        }

        overlay.setOnClickListener {
            hideDialog(navDrawer)
        }

        overlay.visibility = View.VISIBLE
        dialog.visibility = View.VISIBLE

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

    private fun showViewsMenu(
        dialog: View = viewsMenu
    ) {

        backPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            hideViewsMenu(dialog)
        }

        overlay.setOnClickListener {
            hideViewsMenu()
        }

        overlay.visibility = View.VISIBLE
        dialog.visibility = View.VISIBLE

        val transparencyValueHolder = PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            dialog,
            transparencyValueHolder
        )
        animator.start()
    }

    private fun exitApp() {
        if (exitAppToastStillShowing) {
            requireActivity().finish()
            return
        }

        Toast.makeText(this.requireContext(), "press again to exit", Toast.LENGTH_SHORT)
            .show()
        exitAppToastStillShowing = true
        exitAppTimer.start()
    }

    private fun updateWeatherLink() {
        try {
            viewModel.getSharedWeatherLink(currentLocation.latitude, currentLocation.longitude)
        }catch (e:Exception){
            Toast.makeText(
                requireContext(),
                "You need to accept location permission request to continue",
                Toast.LENGTH_SHORT
            ).show()
        }

        lifecycleScope.launch {
            viewModel.sharedLinkEvent.collect { linkResponse ->
                when (linkResponse) {
                    is ShareLinkEvents.Successful -> {

                        linkResponse.data.data?.link?.let {
                            getShareIntent(it)
                        } ?: getShareIntent(URL_TO_SHARE)
                    }
                    is ShareLinkEvents.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            "Current Location Link Not Available...",
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

    override fun onSavedLocationClicked(lat: Double, long: Double) {
        viewModel.updateSavedWeatherView(lat, long)
    }

    private fun setTodayView() {
        viewModel.showTodayView()
        hideViewsMenu()
    }

    private fun setTomorrowView() {
        viewModel.showTomorrowView()
        hideViewsMenu()
    }

    private fun setThisWeekView() {
        viewModel.showThisWeekView()
        hideViewsMenu()
    }

}