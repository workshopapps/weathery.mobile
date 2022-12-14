package com.gear.weathery

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.asLiveData
import com.gear.weathery.dashboard.R
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.navigation.SharedPreference
import com.gear.weathery.common.preference.SettingsPreference
import com.gear.weathery.dashboard.ui.DashboardViewModel
import com.gear.weathery.databinding.ActivityMainBinding
import com.gear.weathery.setting.notifications.database.NotificationDao
import com.google.firebase.messaging.FirebaseMessaging
import com.gear.weathery.location.api.LocationsRepository
import com.gear.weathery.setting.notifications.network.NetworkApi
import com.gear.weathery.setting.notifications.network.NetworkService
import com.gear.weathery.setting.util.Constants.CHANNEL_DESCRIPTION
import com.gear.weathery.setting.util.Constants.CHANNEL_ID
import com.gear.weathery.setting.util.Constants.CHANNEL_NAME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var popUpNotification: ConstraintLayout
    private var rebuild = false
    private lateinit var viewModel:DashboardViewModel
    private lateinit var overlay: View
    private lateinit var backPressedCallback: OnBackPressedCallback

    @Inject
    lateinit var notificationDao: NotificationDao

    @Inject
    lateinit var settingsPreference: SettingsPreference

    @Inject
    lateinit var locationsRepository: LocationsRepository


    private val notificationTimer = object : CountDownTimer(4000, 2000) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            popUpNotification.visibility = View.INVISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProviderFactory = DashboardViewModel.DashboardViewModelFactory(locationsRepository, notificationDao, settingsPreference)

        viewModel = ViewModelProvider(this,viewModelProviderFactory)[DashboardViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        popUpNotification = binding.popupNotification
        overlay = binding.overlayView

        backPressedCallback = onBackPressedDispatcher.addCallback(this) {

        }
        backPressedCallback.isEnabled = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        val fragHost = supportFragmentManager.findFragmentById(com.gear.weathery.R.id.fragHost) as NavHostFragment
        navController = fragHost.findNavController()

        FirebaseMessaging.getInstance().token.addOnSuccessListener(this) { token ->
            Log.d("newToken", token)
            lifecycleScope.launch {
                try {
                    val currentLocation = locationsRepository.locations.first().first()
                    val networkResponse = NetworkApi.retrofitService.subscribeNotifications(token, currentLocation.latitude, currentLocation.longitude)
                    Log.e("xsubscription", networkResponse)
                } catch (e: Exception){
                  Log.e("newToken", "error: $e")
                }
            }
        }

        setContentView(binding.root)
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        val first  = intent.getBooleanExtra("FIRST",true)
        val themeChange = SharedPreference.getBoolean("THEMECHANGE",false)
        val langChange = SharedPreference.getBoolean("CHANGELANGUAGE",false)
        Log.d("TAGf", "onStart: $themeChange")
        //go to dashboard if not first and not theme change
               if(!first && !themeChange && !rebuild && !langChange){
                   val check = SharedPreference.getBoolean("THEMECHANGE",false)
            navController.navigate(R.id.dashboard_nav_graph)
               }else{
                   SharedPreference.putBoolean("THEMECHANGE",false)
                   SharedPreference.putBoolean("CHANGELANGUAGE",false)
               }

        notificationDao.getNotifications().asLiveData().observe(this){
            lifecycleScope.launch {

                val notificationsAlreadyRead = settingsPreference.unreadNotificationCounterFlow().first() == 0

                if(it.isEmpty() || notificationsAlreadyRead){
                    return@launch
                }

                val notification = it.last()
                binding.notificationBodyTextView.text = notification.notificationText
                binding.popupNotification.visibility = View.VISIBLE
                showDialog()
            }

        }

        binding.gotItButtonFrameLayout.setOnClickListener{
            hidePopup()
        }

        lifecycleScope.launch{
            settingsPreference.updateAppForegroundStatus(true)
        }
    }

    override fun onStop() {
        super.onStop()

        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            settingsPreference.updateAppForegroundStatus(false)
        }

    }

    override fun onPause() {
        super.onPause()
        rebuild = true
    }

    override fun onResume() {
        super.onResume()
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = notificationManager.getNotificationChannel(CHANNEL_ID)
            val toneUri = channel.sound
            val toneName =
                RingtoneManager.getRingtone(this, toneUri).getTitle(this)
            lifecycleScope.launch {
                settingsPreference.setNotificationTone(toneName)
            }
            lifecycleScope.launch {
                settingsPreference.togglePushNotification(channel.importance != NotificationManager.IMPORTANCE_NONE)
            }
            lifecycleScope.launch {
                settingsPreference.toggleBanner(channel.importance == NotificationManager.IMPORTANCE_HIGH)
            }
       }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
            }

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            val toneUri = channel.sound
            val tone = RingtoneManager.getRingtone(this, toneUri)
            val toneName = tone.getTitle(this)

            lifecycleScope.launch {
                settingsPreference.setNotificationTone(toneName)
            }

        }
    }

    private fun hidePopup(dialog: View = popUpNotification) {
        overlay.visibility = View.GONE
        backPressedCallback.isEnabled = false

        val movePropertyValueHolder =
            PropertyValuesHolder.ofFloat(View.TRANSLATION_Y , 0f, dialog.height.toFloat())
        val transparencyValueHolder = PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            dialog,
            movePropertyValueHolder,
            transparencyValueHolder
        )
        animator.start()

    }

    private fun showDialog(dialog: View = popUpNotification) {

        backPressedCallback.isEnabled = true


        overlay.visibility = View.VISIBLE
        dialog.visibility = View.VISIBLE

        val movePropertyValueHolder =
            PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, dialog.height.toFloat(), 0f)
        val transparencyValueHolder = PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            dialog,
            movePropertyValueHolder,
            transparencyValueHolder
        )
        animator.start()
    }
}