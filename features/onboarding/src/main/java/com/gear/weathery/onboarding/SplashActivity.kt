package com.gear.weathery.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import kotlinx.coroutines.*

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    val activityScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        SharedPreference.init(applicationContext)
        val allowsPermission : Boolean = SharedPreference.getBoolean("ALLOW", true)
        activityScope.launch {
            delay(3000)
            if(allowsPermission){
                // go to onboarding
            }else{
                //go to dashboard
            }

            finish()
        }

    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }
}