package com.gear.weathery.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.gear.weathery.signin.databinding.FragmentForgotPasswordBinding
import com.gear.weathery.signin.databinding.FragmentSignInBinding
import com.gear.weathery.signin.databinding.FragmentSignUpBinding

class ForgotPasswordFragment : Fragment() {
    private lateinit var backButton: ImageView
    private lateinit var sendButton: Button

    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton = binding.backButton
        sendButton = binding.sendButton

        backButton.setOnClickListener{
            findNavController().navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSignInFragment())
        }

        sendButton.setOnClickListener{
            findNavController().navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToFragmentOtpVerification())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }
}