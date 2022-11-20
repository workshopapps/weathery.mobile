package com.gear.weathery.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.gear.weathery.signin.databinding.FragmentOtpVerificationBinding

class FragmentOtpVerification : Fragment() {
    private lateinit var cancelOtp: ImageView
    private lateinit var verifyOtp: Button

    private lateinit var binding: FragmentOtpVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancelOtp = binding.backButton2
        verifyOtp = binding.verifyButton

        cancelOtp.setOnClickListener{
            findNavController().navigate(FragmentOtpVerificationDirections.actionFragmentOtpVerificationToForgotPasswordFragment())
        }

        verifyOtp.setOnClickListener{
            findNavController().navigate(FragmentOtpVerificationDirections.actionFragmentOtpVerificationToFragmentResetPassword())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otp_verification, container, false)
    }
}