package com.gear.weathery.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.gear.weathery.signin.databinding.FragmentResetPasswordBinding

class FragmentResetPassword : Fragment() {
    private lateinit var cancelPasswordReset: ImageView

    private lateinit var binding: FragmentResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancelPasswordReset = binding.backButton3
        cancelPasswordReset.setOnClickListener{
            findNavController().navigate(FragmentResetPasswordDirections.actionFragmentResetPasswordToForgotPasswordFragment())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }
}