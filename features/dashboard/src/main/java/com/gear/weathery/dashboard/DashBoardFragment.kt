package com.gear.weathery.dashboard

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gear.weathery.common.navigation.AddRemoveLocationNavigation
import com.gear.weathery.common.navigation.BoardingNavigation
import com.gear.weathery.common.navigation.SettingsNavigation
import com.gear.weathery.dashboard.databinding.FragmentDashBoardBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashBoardFragment : Fragment() {
    private var _binding:FragmentDashBoardBinding? =  null
    private val binding get() = _binding!!
    private lateinit var backPressedCallback: OnBackPressedCallback

    private lateinit var navDrawer: ConstraintLayout
    private lateinit var overlay: View


//    @Inject
//    lateinit var boardingNavigation: BoardingNavigation

    @Inject
    lateinit var settingsNavigation: SettingsNavigation

//    @Inject
//    lateinit var signinNavigation: Sign

    @Inject
    lateinit var locationsNavigation: AddRemoveLocationNavigation


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.locationsMenuItemLinearLayout?.setOnClickListener{
            navigateToLocation()
        }

        _binding?.settingsMenuItemLinearLayout?.setOnClickListener {
            navigateToSettings()
        }

        _binding?.signInMenuItemLinearLayout?.setOnClickListener {
            navigateToSignin()
        }

        navDrawer = binding.navDrawerConstraintLayout
        overlay = binding.overlayView

        binding.navDrawerConstraintLayout.setOnClickListener {
            showDialog(navDrawer)
        }

//        overlay.setOnClickListener {
//            hideDialog(navDrawer)
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()
        }
        backPressedCallback.isEnabled = true
    }



    fun navigateToSignin(){

    }

    fun navigateToSettings(){
        settingsNavigation.navigateToSettings(navController = findNavController())
    }

    fun navigateToNotifications(){

    }

    fun navigateToLocation(){
        locationsNavigation.navigateToAddRemoveLocation(navController = findNavController())
    }

    private fun hideDialog(dialog: ConstraintLayout) {
        overlay.visibility = View.GONE
        backPressedCallback.isEnabled = false

        val movePropertyValueHolder = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f, -dialog.height.toFloat())
        val transparencyValueHolder = PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            dialog,
            movePropertyValueHolder,
            transparencyValueHolder
        )
        animator.start()
    }

    private fun showDialog(dialog: ConstraintLayout) {

        backPressedCallback.isEnabled = true
        overlay.visibility = View.VISIBLE

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

}