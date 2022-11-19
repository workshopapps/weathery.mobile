package com.gear.weathery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.gear.weathery.common.navigation.BoardingNavigation
import com.gear.weathery.onboarding.SharedPreference
import kotlinx.coroutines.*
import javax.inject.Inject

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var boardingNavigation: BoardingNavigation
    val activityScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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