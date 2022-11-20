package com.gear.weathery.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.gear.weathery.signin.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private lateinit var forgotPassword: TextView
    private lateinit var createAccount: TextView

    private lateinit var binding: FragmentSignInBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forgotPassword = binding.forgotPassword
        forgotPassword.setOnClickListener{
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToForgotPasswordFragment())
        }

        createAccount = binding.createNewAccount
        createAccount.setOnClickListener{
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment3())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }
}