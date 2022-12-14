package com.gear.weathery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
//import com.gear.weathery.common.preference.SettingsPreference
import com.gear.weathery.common.navigation.BoardingNavigation
import com.gear.weathery.common.navigation.DashBoardNavigation
import com.gear.weathery.common.navigation.SharedPreference
import com.gear.weathery.common.preference.SettingsPreference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var boardingNavigation: BoardingNavigation

    @Inject
    lateinit var dashBoardNavigation: DashBoardNavigation

    @Inject
    lateinit var settingsPreference: SettingsPreference

    private val activityScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        SharedPreference.init(applicationContext)

        val first : Boolean = SharedPreference.getBoolean("FIRST", true)

        activityScope.launch {
            delay(2000)
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