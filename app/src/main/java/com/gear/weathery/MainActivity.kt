package com.gear.weathery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.gear.weathery.dashboard.R
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.navigation.SharedPreference
import com.gear.weathery.common.preference.SettingsPreference
import com.gear.weathery.dashboard.ui.DashboardViewModel
import com.gear.weathery.databinding.ActivityMainBinding
import com.gear.weathery.location.api.LocationsRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var viewModel:DashboardViewModel

    @Inject
    lateinit var settingsPreference: SettingsPreference
    @Inject
    lateinit var locationsRepository: LocationsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProviderFactory = DashboardViewModel.DashboardViewModelFactory(locationsRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory)[DashboardViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragHost = supportFragmentManager.findFragmentById(com.gear.weathery.R.id.fragHost) as NavHostFragment
        navController = fragHost.findNavController()




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

    }
}