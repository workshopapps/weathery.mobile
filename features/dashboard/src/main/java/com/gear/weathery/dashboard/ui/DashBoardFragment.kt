package com.gear.weathery.dashboard.ui

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.navigation.*
import com.gear.weathery.common.preference.SettingsPreference
import com.gear.weathery.dashboard.LocationPermissionFragment
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
import com.gear.weathery.setting.notifications.database.NotificationDao
import com.gear.weathery.setting.notifications.utils.processNotificationData
import com.gear.weathery.setting.notifications.utils.sendNotification
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

const val REQUEST_LOCATION_SETTINGS = 25


@AndroidEntryPoint
class DashBoardFragment : Fragment(), OnClickEvent {
    var checkState: Boolean = true
    private lateinit var binding: FragmentDashBoardBinding
    private var permissionGranted = SharedPreference.getBoolean("ALLOWPERMISSION", false)

    @Inject
    lateinit var locationsRepository: LocationsRepository

    @Inject
    lateinit var notificationDao: NotificationDao

    private val viewModel: DashboardViewModel by activityViewModels {
        DashboardViewModelFactory(
            locationsRepository, notificationDao, settingsPreference
        )
    }
    private val adapter = TimelineRecyclerAdapter()

    private lateinit var backPressedCallback: OnBackPressedCallback

    private lateinit var navDrawer: ConstraintLayout
    private lateinit var overlay: View
    private lateinit var viewsMenu: LinearLayout

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @Inject
    lateinit var settingsNavigation: SettingsNavigation

    @Inject
    lateinit var notificationsNavigation: NotificationsNavigation

    @Inject
    lateinit var signInNavigation: SignInNavigation

    @Inject
    lateinit var settingsPreference: SettingsPreference

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

        Log.d("CheckForPermissionw", "onCreate: $permissionGranted")

        backPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            exitApp()
        }
        backPressedCallback.isEnabled = true

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        lifecycleScope.launch {
            settingsPreference.incrementAppStartsCount()
        }

    }

    private fun turnOnLocationSettings() {
        val locationRequestBuilder = LocationRequest.Builder(10000)
            .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            .setMinUpdateIntervalMillis(5000)

        val locationSettingsRequestBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequestBuilder.build())

        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> =
            client.checkLocationSettings(locationSettingsRequestBuilder.build())

        task.addOnSuccessListener {
            // All location settings are satisfied. The client can initialize
            // location requests here.

            retrieveLocationAndUpdateWeather()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
//                    exception.startResolutionForResult(requireActivity(), REQUEST_LOCATION_SETTINGS)
                    startIntentSenderForResult(
                        exception.resolution.intentSender,
                        REQUEST_LOCATION_SETTINGS,
                        null,
                        0,
                        0,
                        0,
                        null
                    );
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun retrieveLocationAndUpdateWeather() {
        val cts = CancellationTokenSource()
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cts.token)
            .addOnSuccessListener {
                if (it == null) {
                    return@addOnSuccessListener
                }

                viewModel.updateDeviceLocation(it)

                lifecycleScope.launch {
                    val savedLocation = com.gear.weathery.location.api.Location(
                        id = 1, state = "",
                        name = "current location", country = "", longitude = it.longitude,
                        latitude = it.latitude
                    )
                    locationsRepository.saveLocation(savedLocation)
                }
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_LOCATION_SETTINGS) {
            retrieveLocationAndUpdateWeather()
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
        turnOnLocationSettings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.locationHeaderLinearLayout.setOnClickListener {
            BottomSheetDrawer().show(childFragmentManager, "BOTTOM SHEET")
        }

        binding.notificationButtonGroupConstraintLayout.setOnClickListener {
            navigateToNotifications()
        }

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

//        binding.thisWeekGroupLinearLayout.setOnClickListener {
//            setThisWeekView()
//        }

        viewModel.currentWeatherStatus.observe(viewLifecycleOwner) {
            updateViewsForNewCurrentWeatherStatus(it)
        }

        viewModel.timelineStatus.observe(viewLifecycleOwner) {
            updateViewsForNewTimelineStatus(it)
        }

        viewModel.notificationsNumber.observe(viewLifecycleOwner) {
            if (it == null || it == 0) {
                binding.notificationCounterFrameLayout.visibility = View.GONE
            } else {
                binding.notificationCounterFrameLayout.visibility = View.VISIBLE
                binding.notificationCounterTextView.text = it.toString()
            }
        }

        binding.tryAgainButtonTextView.setOnClickListener {
            retrieveLocationAndUpdateWeather()
        }

        binding.feedbackButtonFrameLayout.setOnClickListener {
            hideDialog(navDrawer)
            FeedbackFragment().show(childFragmentManager, "random text")
        }

        settingsPreference.appStartsCountFlow().asLiveData().observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                val autoFeedbackPromptAlreadyPrompted =
                    settingsPreference.getAutoFeedbackAlreadyPromptedStatus()
                val appStartsCountIsTwo = it == 2

                if (appStartsCountIsTwo && !autoFeedbackPromptAlreadyPrompted) {
                    FeedbackFragment().show(childFragmentManager, "random text")
                }

                if (appStartsCountIsTwo){
                    settingsPreference.updateAutoFeedbackPrompted(true)
                }
            }
        }


    }


    private fun updateViewsForNewCurrentWeatherStatus(newCurrentWeatherStatus: Int?) {
        if (newCurrentWeatherStatus == null) {
            return
        }

        when (newCurrentWeatherStatus) {

            BUSY -> {
                binding.todayForecastShimmer.visibility = View.VISIBLE
                binding.todayForecastShimmer.startShimmer()
                //binding.currentWeatherLoadingTextView.visibility = View.VISIBLE
                binding.currentWeatherGroupLinearLayout.visibility = View.INVISIBLE
                binding.currentWeatherDefaultTextView.visibility = View.GONE
                binding.currentWeatherErrorTextView.visibility = View.GONE
                binding.tryAgainButtonTextView.visibility = View.GONE
                updateViewEnabledState(false)
            }

            PASSED -> {
                binding.todayForecastShimmer.visibility = View.GONE
                binding.todayForecastShimmer.stopShimmer()
                binding.currentWeatherLoadingTextView.visibility = View.GONE
                binding.currentWeatherGroupLinearLayout.visibility = View.VISIBLE
                binding.currentWeatherDefaultTextView.visibility = View.GONE
                binding.currentWeatherErrorTextView.visibility = View.GONE
                binding.tryAgainButtonTextView.visibility = View.GONE
                updateViewEnabledState(true)
            }

            FAILED -> {
                binding.todayForecastShimmer.visibility = View.GONE
                binding.todayForecastShimmer.stopShimmer()
                binding.currentWeatherLoadingTextView.visibility = View.GONE
                binding.currentWeatherGroupLinearLayout.visibility = View.INVISIBLE
                binding.currentWeatherDefaultTextView.visibility = View.GONE
                binding.currentWeatherErrorTextView.visibility = View.VISIBLE
                binding.tryAgainButtonTextView.visibility = View.VISIBLE
                updateViewEnabledState(false)
            }

            DEFAULT -> {
                binding.currentWeatherLoadingTextView.visibility = View.GONE
                binding.currentWeatherGroupLinearLayout.visibility = View.INVISIBLE
                binding.currentWeatherDefaultTextView.visibility = View.VISIBLE
                binding.currentWeatherErrorTextView.visibility = View.GONE
                binding.tryAgainButtonTextView.visibility = View.GONE
                updateViewEnabledState(false)
            }
        }
    }

    private fun updateViewsForNewTimelineStatus(newTimelineStatus: Int?) {
        if (newTimelineStatus == null) {
            return
        }

        when (newTimelineStatus) {
            BUSY -> {
                binding.timelineShimmer.visibility = View.VISIBLE
                binding.timelineShimmer.startShimmer()
                //binding.timelineLoadingTextView.visibility = View.VISIBLE
                binding.timelineRecyclerView.visibility = View.GONE
                binding.timelineErrorTextView.visibility = View.GONE
                binding.timelineDefaultTextView.visibility = View.GONE
                binding.tryAgainButtonTextView.visibility = View.GONE
                updateViewEnabledState(false)
            }

            PASSED -> {
                binding.timelineShimmer.visibility = View.GONE
                binding.timelineShimmer.stopShimmer()
                binding.timelineLoadingTextView.visibility = View.GONE
                binding.timelineRecyclerView.visibility = View.VISIBLE
                binding.timelineErrorTextView.visibility = View.GONE
                binding.timelineDefaultTextView.visibility = View.GONE
                binding.tryAgainButtonTextView.visibility = View.GONE
                updateViewEnabledState(true)
            }

            FAILED -> {
                binding.timelineShimmer.visibility = View.GONE
                binding.timelineShimmer.stopShimmer()
                binding.timelineLoadingTextView.visibility = View.GONE
                binding.timelineRecyclerView.visibility = View.GONE
                binding.timelineErrorTextView.visibility = View.VISIBLE
                binding.timelineDefaultTextView.visibility = View.GONE
                binding.tryAgainButtonTextView.visibility = View.VISIBLE
                updateViewEnabledState(false)
            }

            DEFAULT -> {
                binding.timelineLoadingTextView.visibility = View.GONE
                binding.timelineRecyclerView.visibility = View.GONE
                binding.timelineErrorTextView.visibility = View.GONE
                binding.timelineDefaultTextView.visibility = View.VISIBLE
                binding.tryAgainButtonTextView.visibility = View.GONE
                updateViewEnabledState(false)
            }
        }
    }

    private fun updateViewsForNewViewMode(newViewMode: String?) {
        if (newViewMode == null) {
            return
        }

        when (newViewMode) {
            TODAY_VIEW_MODE -> {
                binding.apply {
                    currentViewTextView.text = getString(R.string.today)
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
//                    thisWeekTextView.setTextColor(resources.getColor(R.color.default_colour))
//                    thisWeekIconImageView.setColorFilter(
//                        ContextCompat.getColor(
//                            requireContext(),
//                            R.color.default_colour
//                        ), android.graphics.PorterDuff.Mode.SRC_IN
//                    )
                }
            }

            TOMORROW_VIEW_MODE -> {
                binding.apply {
                    currentViewTextView.text = getString(R.string.tomorrow)
                    tomorrowTextView.setTextColor(resources.getColor(R.color.weathery_orange))
                    tomorrowIconImageView.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.weathery_orange
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
//                    thisWeekTextView.setTextColor(resources.getColor(R.color.default_colour))
//                    thisWeekIconImageView.setColorFilter(
//                        ContextCompat.getColor(
//                            requireContext(),
//                            R.color.default_colour
//                        ), android.graphics.PorterDuff.Mode.SRC_IN
//                    )
                    todayTextView.setTextColor(resources.getColor(R.color.default_colour))
                    todayIconImageView.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.default_colour
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                }
            }

//            THIS_WEEK_VIEW_MODE -> {
//                binding.apply {
//                    currentViewTextView.text = getString(R.string.thisweek)
//                    thisWeekTextView.setTextColor(resources.getColor(R.color.weathery_orange))
//                    thisWeekIconImageView.setColorFilter(
//                        ContextCompat.getColor(
//                            requireContext(),
//                            R.color.weathery_orange
//                        ), android.graphics.PorterDuff.Mode.SRC_IN
//                    )
//                    tomorrowTextView.setTextColor(resources.getColor(R.color.default_colour))
//                    tomorrowIconImageView.setColorFilter(
//                        ContextCompat.getColor(
//                            requireContext(),
//                            R.color.default_colour
//                        ), android.graphics.PorterDuff.Mode.SRC_IN
//                    )
//                    todayTextView.setTextColor(resources.getColor(R.color.default_colour))
//                    todayIconImageView.setColorFilter(
//                        ContextCompat.getColor(
//                            requireContext(),
//                            R.color.default_colour
//                        ), android.graphics.PorterDuff.Mode.SRC_IN
//                    )
//                }
//            }

        }
    }

    private fun updateViewsForNewCurrentWeather(newCurrentWeather: WeatherCondition?) {
        if (newCurrentWeather == null) {
            return
        }

        binding.stateAndCountryTextView.text =
            "${newCurrentWeather.state}, ${newCurrentWeather.country}"
        binding.currentWeatherTextView.text = newCurrentWeather.main
        val startTime = getTimeForDisplay(newCurrentWeather.timeInMillis)
        val endTime = getTimeForDisplay(newCurrentWeather.endTimeTimeInMillis)
        binding.currentWeatherTimeTextView.text = "$startTime to $endTime"

        // binding.currentWeatherRiskTextView.text = newCurrentWeather.risk
        if (newCurrentWeather.risk == NONE) {
            binding.currentWeatherRiskTextView.text = "RISK : NONE"
        } else {
            binding.currentWeatherRiskTextView.text =
                "RISK : ${(newCurrentWeather.risk)?.toUpperCase(Locale.ROOT)}"
        }

        //  binding.currentWeatherRiskTextView.text = if(newCurrentWeather.risk != "null") newCurrentWeather.risk else "RISK : NONE"
        binding.locationTextView.text =
            "${newCurrentWeather.state}, ${newCurrentWeather.country}"
        binding.currentWeatherRiskIndicatorImageView.setImageResource(if (newCurrentWeather.risk == NONE) R.drawable.ic_warning_inactive else R.drawable.ic_warning_active)
        binding.currentWeatherIconId.setImageResource(
            when (newCurrentWeather.main) {
                "Drizzle", "Freezing Drizzle", "Freezing rain" -> R.drawable.sun_cloud_rain
                "Rain", "Rain showers", "Thunderstorm" -> R.drawable.cloud_rain
                "Fog and depositing rime fog" -> R.drawable.cloud
                "Scattered clouds", "Few clouds", "Broken clouds" -> R.drawable.sun_cloud
                else -> R.drawable.sun
            }
        )
        binding.currentWeatherRiskIndicatorImageView.setImageResource(if (newCurrentWeather.risk == NONE) R.drawable.ic_warning_inactive else R.drawable.ic_warning_active)

    }

    private fun updateViewsForNewTimeline(newTimeLine: Pair<List<TimelineWeather>, String>) {
        adapter.updateItemList(newTimeLine.first, newTimeLine.second)
    }

    private fun updateViewEnabledState(newState: Boolean) {
//        binding.locationHeaderLinearLayout.isEnabled = newState
        binding.timelineViewsMenuImageView.isEnabled = newState
        binding.shareButtonImageView.isEnabled = newState
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

    private fun hideViewsMenu(dialog: View = viewsMenu) {
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

    private fun showViewsMenu(dialog: View = viewsMenu) {

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

        Toast.makeText(this.requireContext(), getString(R.string.exit), Toast.LENGTH_SHORT)
            .show()
        exitAppToastStillShowing = true
        exitAppTimer.start()
    }

    private fun updateWeatherLink() {
        try {
            viewModel.getSharedWeatherLink()
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                getString(R.string.locationpermission),
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
                            getString(R.string.notAvailable),
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

    override fun onSavedLocationClicked(lat: Double, long: Double) {
        viewModel.updateSelectedLocation(lat, long)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!permissionGranted) {
            if (viewModel.getLatLongSelectedLocation() == null) {
                viewModel.setDefaultMode()
                val btmDialog: LocationPermissionFragment = LocationPermissionFragment()
                btmDialog.setCancelable(true)
                btmDialog.show(childFragmentManager, "LOCATION DIALOG")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        permissionGranted = SharedPreference.getBoolean("ALLOWPERMISSION", false)
        val requiredPermission = Manifest.permission.ACCESS_COARSE_LOCATION
        val checkVal = requireContext().checkCallingOrSelfPermission(requiredPermission)
        if (checkVal == PackageManager.PERMISSION_GRANTED) {
            SharedPreference.putBoolean("ALLOWPERMISSION", true)
            retrieveLocationAndUpdateWeather()
        }
        Log.d("CheckForPermissionw", "onResume: $permissionGranted ")
    }


}