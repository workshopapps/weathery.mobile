package com.gear.weathery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import com.gear.weathery.dashboard.R
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.navigation.SharedPreference
import com.gear.weathery.common.preference.SettingsPreference
import com.gear.weathery.databinding.ActivityMainBinding
import com.gear.weathery.setting.notifications.database.NotificationDao
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var popUpNotification: ConstraintLayout

    @Inject
    lateinit var notificationDao: NotificationDao

    @Inject
    lateinit var settingsPreference: SettingsPreference


    private val notificationTimer = object : CountDownTimer(2000, 1000) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            popUpNotification.visibility = View.INVISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        popUpNotification = binding.popupNotification
        setContentView(binding.root)

        val fragHost = supportFragmentManager.findFragmentById(com.gear.weathery.R.id.fragHost) as NavHostFragment
        navController = fragHost.findNavController()

        FirebaseMessaging.getInstance().token.addOnSuccessListener(this) { token ->
            Log.d("newToken", token)
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        val first  = intent.getBooleanExtra("FIRST",true)
        val themeChange = SharedPreference.getBoolean("THEMECHANGE",false)
        Log.d("TAGf", "onStart: $themeChange")
        //go to dashboard if not first and not theme change
               if(!first && !themeChange){
                   val check = SharedPreference.getBoolean("THEMECHANGE",false)
            navController.navigate(R.id.dashboard_nav_graph)
               }else{
                   SharedPreference.putBoolean("THEMECHANGE",false)
               }

        notificationDao.getNotifications().onEach {
            if (it.isEmpty()){
                return@onEach
            }
            val notification = it.last()
            binding.notificationBodyTextView.text = notification.notificationText
            binding.popupNotification.visibility = View.VISIBLE
            notificationTimer.start()
        }.launchIn(lifecycleScope)


    }
}