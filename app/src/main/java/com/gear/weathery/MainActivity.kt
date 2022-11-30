package com.gear.weathery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.preference.SettingsPreference
import com.gear.weathery.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var settingsPreference: SettingsPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val fragHost = supportFragmentManager.findFragmentById(R.id.fragHost) as NavHostFragment
        navController = fragHost.findNavController()
        var first  = intent.getBooleanExtra("FIRST",true)

        /*if(!first){

            navController.navigate(dashboard_nav_graph)
           // navController.clearBackStack(dashboard_nav_graph)
        }*/

    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}