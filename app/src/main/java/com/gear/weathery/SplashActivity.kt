package com.gear.weathery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
//import com.gear.weathery.common.preference.SettingsPreference
import com.gear.weathery.common.navigation.BoardingNavigation
import com.gear.weathery.common.navigation.SharedPreference
import com.gear.weathery.common.preference.SettingsPreference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var boardingNavigation: BoardingNavigation

    @Inject
    lateinit var settingsPreference: SettingsPreference

    val activityScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            settingsPreference.darkMode().collect{theme ->
                when (theme) {
                    "light" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    "dark" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    "system" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                }
            }
        }
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        SharedPreference.init(applicationContext)
        val first : Boolean = SharedPreference.getBoolean("ALLOW", true)
        activityScope.launch {
            delay(3000)
            if(first){
                val intent:Intent = Intent(this@SplashActivity,MainActivity::class.java)
                intent.putExtra("FIRST",first)
                startActivity(intent)
            }else{
               val intent:Intent = Intent(this@SplashActivity,MainActivity::class.java)
                intent.putExtra("FIRST",first)
                startActivity(intent)
            }

            finish()
        }

    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }
}